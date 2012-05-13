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
* Date: 5/13/12
* Time: 4:12 PM
*/

package com.github.amon.rpc

import com.github.amon.Node
import com.codahale.jerkson.Json._

object JsonUtil {

  def toJson(node: Node) = generate(node.store.properties)

  def json[A](obj: A) = generate(obj)
}