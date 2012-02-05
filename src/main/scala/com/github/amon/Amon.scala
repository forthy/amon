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
* Date: 1/30/12
* Time: 7:31 PM
*/

package com.github.amon

import bytecask.Bytecask
import cluster.{Server, Clustered}
import rpc._
import scopt.OptionParser
import com.google.common.io.Files

class Amon(port: Int, dir: String) extends Clustered with Logging {
  val db = new Bytecask(dir)
  val services = new Services(port)

  services.configure {
    request =>
      request match {
        case GET(Path(Seg("data" :: id :: Nil))) => {
          debug("get " + id)
          val value = db.get(id.getBytes)
          if (!value.isEmpty)
            BinaryResponse(value.get)
          else
            EmptyResponse(404)
        }
        case POST(Path(Seg("data" :: id :: Nil))) => {
          debug("post " + id)
          db.put(id.getBytes, request.content)
          TextResponse("post: OK".getBytes)
        }
        case DELETE(Path(Seg("data" :: id :: Nil))) => {
          debug("delete " + id)
          val value = db.delete(id.getBytes)
          if (!value.isEmpty)
            TextResponse("delete: OK".getBytes)
          else
            EmptyResponse(404)
        }
        case GET(Path(Seg("merge" :: Nil))) => {
          debug("merge")
          db.merge()
          TextResponse("merge: OK".getBytes)
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
    db.close()
    disconnectCluster()
    stopIntrumenting()
  }

  def membersRegistered(members: Set[Server]) {
    info("+ " + members)
  }

  def membersLost(members: Set[Server]) {
    info("- " + members)
  }
}

object AmonStandalone extends Logging {

  def main(args: Array[String]) {
    var port = 9090
    var path = Files.createTempDir().getAbsolutePath
    val parser = new OptionParser("amon") {
      intOpt("p", "port", "port", {
        v: Int => port = v
      })
      opt("f", "path", "<path>", "Bytecask work directory", {
        v: String => path = v
      })
    }
    if (parser.parse(args)) {
      info("Starting amon: port %s, path: %s".format(port, path))
      val amon = new Amon(port, path)
      amon.start()
      System.in.read()
      amon.stop()
    }
  }
}