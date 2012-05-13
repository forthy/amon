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
* Date: 7/28/11
* Time: 8:32 PM
*/

package com.github.amon.cluster

import java.util.concurrent.atomic.AtomicLong
import com.github.amon.Logging
import java.util.UUID

trait Instrumented extends Logging {

  val startTime = new AtomicLong
  val opCounter = new AtomicLong

  val nodeId = UUID.randomUUID().toString

  def startIntrumenting() {
    startTime.set(System.currentTimeMillis())
  }

  def stopIntrumenting() {
  }

  def uptime = if (startTime.get > 0) System.currentTimeMillis() - startTime.get else 0

  def tick() {
    opCounter.incrementAndGet()
  }

  def opCount = opCounter.get

  //TODO JMX
}