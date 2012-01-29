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
* Time: 6:32 PM
*/

package com.github.amon.rpc


class Method(method: String) {
  def unapply(request: Request) =
    if (request.method.equalsIgnoreCase(method)) Some(request)
    else None
}

object GET extends Method("GET")

object POST extends Method("POST")

object PUT extends Method("PUT")

object DELETE extends Method("DELETE")

object HEAD extends Method("HEAD")

object CONNECT extends Method("CONNECT")

object OPTIONS extends Method("OPTIONS")

object TRACE extends Method("TRACE")