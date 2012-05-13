/*
* Copyright 2011 P.Budzik
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* User: przemek
* Date: 2/25/12
* Time: 5:10 PM
*/

package com.github.amon

import cluster.{Server, Clustered}
import com.github.bytecask.Bytes._
import rpc._
import com.github.bytecask.Utils
import com.github.amon.rpc.JsonUtil._

class Node(port: Int, val store: Store) extends Clustered with Logging {
  val services = new Services(port)
  val peers = collection.mutable.Set[Server]()

  services.configure {
    request =>
      request match {
        case GET(Path(Seg("data" :: id :: Nil))) => {
          debug("get " + id)
          val value = get(request.getMode, id)
          if (!value.isEmpty)
            BinaryResponse(value.get)
          else
            EmptyResponse(404)
        }
        case POST(Path(Seg("data" :: id :: Nil))) => {
          debug("post " + id)
          put(request.getMode, id, request.content)
          TextResponse(json(Map("message" -> "OK", "action" -> "put", "id" -> id)))
        }
        case POST(Path(Seg("data" :: Nil))) => {
          val id = Amon.nextId
          debug("post " + id)
          put(request.getMode, id, request.content)
          TextResponse(json(Map("message" -> "OK", "action" -> "put", "id" -> id)))
        }
        case DELETE(Path(Seg("data" :: id :: Nil))) => {
          debug("delete " + id)
          val value = delete(request.getMode, id)
          if (!value.isEmpty)
            TextResponse(json(Map("message" -> "OK", "action" -> "delete", "id" -> id)))
          else
            EmptyResponse(404)
        }
        case GET(Path(Seg("merge" :: Nil))) => {
          debug("merge")
          store.merge()
          TextResponse(json(Map("message" -> "OK", "action" -> "merge")))
        }
        case GET(Path(Seg("ping" :: Nil))) => {
          debug("ping")
          TextResponse(pingResponse)
        }
        case GET(Path(Seg("console" :: Nil))) => {
          debug("console")
          TextResponse(consoleResponse)
        }
      }
  }

  def start() {
    services.start()
    connectCluster(port)
    startIntrumenting()
  }

  def stop() {
    services.stop()
    store.close()
    disconnectCluster()
    stopIntrumenting()
  }

  private def pingResponse = json(Map("message" -> "pong", "time" -> Utils.now, "uptime" -> uptime,
    "id" -> nodeId, "store" -> store.properties))

  private def consoleResponse = json(Map("message" -> "OK", "store" -> store.properties,
    "nodes" -> peers.mkString("{", ",", "}"), "opcount" -> opCount))

  def membersRegistered(members: Set[Server]) {
    info("+ " + members)
    peers.synchronized(members.foreach(peers.add(_)))
  }

  def membersLost(members: Set[Server]) {
    info("- " + members)
    peers.synchronized(members.foreach(peers.remove(_)))
  }

  def put(mode: Mode, key: Array[Byte], value: Array[Byte]) = {
    tick()
    mode match {
      case INTERNAL => store.put(key, value)
      case EXTERNAL => {
        store.put(key, value)
        //TODO: replicate
      }
    }
  }

  def get(mode: Mode, key: Array[Byte]) = {
    tick()
    mode match {
      case INTERNAL => store.get(key)
      case EXTERNAL => {
        store.get(key)
        //TODO: remote
      }
    }
  }

  def delete(mode: Mode, key: Array[Byte]) = {
    tick()
    mode match {
      case INTERNAL => store.delete(key)
      case EXTERNAL => {
        store.delete(key)
        //TODO: replicate
      }
    }
  }

}

