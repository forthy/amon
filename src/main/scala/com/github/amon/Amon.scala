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

import com.github.bytecask.Bytecask
import org.streum.configrity.Configuration
import java.util.UUID

class Amon(nodes: List[Node]) extends Logging {

  def start() {
    nodes.foreach(_.start())
  }

  def stop() {
    nodes.foreach(_.stop())
  }

}

object Amon {
  def nextId = UUID.randomUUID().toString
}

object AmonStandalone extends Logging {

  def main(args: Array[String]) {
    val config = Configuration.loadResource("/amon.conf")
    val nodes = parseNodes(config[List[String]]("nodes"))
    val amon = new Amon(nodes)
    amon.start()
    System.in.read()
    amon.stop()
  }

  private def parseNodes(nodes: List[String]) = nodes.map {
    s =>
      val Array(t, p, d, _*) = s.trim.split(":")
      debug("Creating node: " + p + ":" + t + ":" + d)
      new Node(p.toInt, createStore(t, d))
  }

  private def createStore(t: String, d: String) = {
    t match {
      case "bytecask" => new BytecaskStore(new Bytecask(dir = d))
      case _ => throw new IllegalArgumentException("Store type: " + t + " not supported")
    }

  }
}

case class Quorum(n: Int)

object DefaultReadQuorum extends Quorum(2)

object DefaultWriteQuorum extends Quorum(2)