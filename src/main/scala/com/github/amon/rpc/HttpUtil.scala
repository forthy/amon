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
* Date: 7/7/11
* Time: 8:36 PM
*/

package com.github.amon.rpc

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.{HttpDelete, HttpGet, HttpPost}
import org.apache.http.entity.{StringEntity, ContentProducer, EntityTemplate}
import com.github.amon.Logging
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpResponse, HttpEntity}
import org.apache.http.message.AbstractHttpMessage

object HttpUtil extends Logging {

  private def getClient = new DefaultHttpClient()

  def get(uri: String, headers: Map[String, String] = Map()) = {
    val client = getClient
    val get = new HttpGet(uri)
    setHeaders(get, headers)
    val response = client.execute(get)
    debug("http response=" + response)
    response
  }

  def post(uri: String, cp: ContentProducer, headers: Map[String, String]) = {
    val client = getClient
    val entity = new EntityTemplate(cp)
    entity.setChunked(true)
    val post = new HttpPost(uri)
    setHeaders(post, headers)
    post.setEntity(entity)
    val response = client.execute(post)
    debug("http response=" + response)
    response
  }

  def post(uri: String, headers: Map[String, String] = Map()) = {
    val client = getClient
    val post = new HttpPost(uri)
    setHeaders(post, headers)
    val response = client.execute(post)
    debug("http response=" + response)
    response
  }

  def post(uri: String, content: String, headers: Map[String, String]) = {
    val client = getClient
    val post = new HttpPost(uri)
    setHeaders(post, headers)
    post.setEntity(new StringEntity(content))
    val response = client.execute(post)
    debug("http response=" + response)
    response
  }

  def delete(uri: String, headers: Map[String, String] = Map()) = {
    val client = getClient
    val delete = new HttpDelete(uri)
    setHeaders(delete, headers)
    val response = client.execute(delete)
    debug("response=" + response)
    response
  }

  private def setHeaders(method: AbstractHttpMessage, headers: Map[String, String]) {
    for ((k, v) <- headers) {
      method.setHeader(k, v)
    }
  }

  implicit def toRichResponse(response: HttpResponse) = new RichHttpResponse(response)

  class RichHttpResponse(response: HttpResponse) {
    def asString = EntityUtils.toString(response.getEntity)
  }

}


