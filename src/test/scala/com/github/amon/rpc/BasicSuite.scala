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
* Date: 7/2/11
* Time: 12:07 PM
*/

package com.github.amon.rpc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class BasicSuite extends FunSuite with ShouldMatchers with BeforeAndAfterEach {

  test("basic foo test") {
    val services = new Services(8080)
    services.configure {
      request =>
        request match {
          case GET(Path(Seg("db" :: id :: Nil))) => {
            println("Get " + id)
            TextResponse(("been called with get/" + id).getBytes)
          }
          case POST(Path(Seg("db" :: id :: Nil))) => {
            println("Post " + id)
            TextResponse(("been called with post/" + id).getBytes)
          }
        }
    }

    services.start()

    // println(EntityUtils.toString(HttpUtil.get("http://localhost:8080/").getEntity))
    println(HttpUtil.get("http://localhost:8080/db/666"))
    println(HttpUtil.post("http://localhost:8080/db/666"))
    services.stop()

  }

}