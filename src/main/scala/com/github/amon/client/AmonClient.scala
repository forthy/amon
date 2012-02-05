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
* Date: 2/5/12
* Time: 9:31 AM
*/

package com.github.amon.client

import com.github.amon.rpc.HttpUtil

class AmonClient(url: String) {

  def put(key: Array[Byte], value: Array[Byte]) = {
    remote {
      val response = HttpUtil.post(url + "/db/" + key)
    }
  }

  def get(key: Array[Byte]) = {
    remote {
      val response = HttpUtil.get(url + "/db/" + key)
    }
  }

  def delete(key: Array[Byte]) = {
    remote {
      val response = HttpUtil.delete(url + "/db/" + key)
    }
  }

  def merge() {
    remote {
      val response = HttpUtil.get(url + "/merge")
    }
  }

  def ping() {
    remote {
      val response = HttpUtil.get(url + "/ping")
    }
  }

  private def remote[T](f: => T) {
    try {
      f
    }
    catch {
      case e: Exception => Left(e)
    }
  }
}