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
import bytecask.Bytecask
import rpc.{MULTI_NODE, SINGLE_NODE, Mode}

class Node(val db: Bytecask) extends Clustered with Logging {
  val servers = collection.mutable.Set[Server]()

  def membersRegistered(members: Set[Server]) {
    info("+ " + members)
    servers.synchronized(members.foreach(servers.add(_)))
  }

  def membersLost(members: Set[Server]) {
    info("- " + members)
    servers.synchronized(members.foreach(servers.remove(_)))
  }

  def put(mode: Mode, key: Array[Byte], value: Array[Byte]) = {
    mode match {
      case SINGLE_NODE => db.put(key, value)
      case MULTI_NODE => {
        db.put(key, value)
        //TODO: replicate
      }
    }
  }

  def get(mode: Mode, key: Array[Byte]) = {
    mode match {
      case SINGLE_NODE => db.get(key)
      case MULTI_NODE => {
        db.get(key)
        //TODO: remote
      }
    }
  }

  def delete(mode: Mode, key: Array[Byte]) = {
    mode match {
      case SINGLE_NODE => db.delete(key)
      case MULTI_NODE => {
        db.delete(key)
        //TODO: replicate
      }
    }
  }

}

