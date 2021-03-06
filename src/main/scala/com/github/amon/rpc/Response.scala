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
* Date: 1/29/12
* Time: 7:07 PM
*/

package com.github.amon.rpc

class Response(val contentType: String, val status: Int = 200, val content: Array[Byte])

//object Response {
//  def apply(s: String): Response = Response(s.getBytes)
//}

case class TextResponse(override val content: Array[Byte]) extends Response("text/plain; charset=UTF-8", 200, content)

case class BinaryResponse(override val content: Array[Byte]) extends Response("application/octet-stream", 200, content)

case class EmptyResponse(override val status: Int) extends Response("", status, Array())