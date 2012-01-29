/*
* Copyright 2011 P.Budzik
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless requestuired by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* User: przemek
* Date: 1/29/12
* Time: 6:35 PM
*/

package com.github.amon.rpc

object Path {
  def unapply(request: Request) = Some(request.uri.split('?')(0))

  def apply(request: Request) = request.uri.split('?')(0)
}

object QueryString {
  def unapply(request: Request) = request.uri.split('?') match {
    case Array(path) => None
    case Array(path, query) => Some(query)
  }
}

object Seg {
  def unapply(path: String): Option[List[String]] = path.split("/").toList match {
    case "" :: rest => Some(rest)
    case all => Some(all)
  }
}

