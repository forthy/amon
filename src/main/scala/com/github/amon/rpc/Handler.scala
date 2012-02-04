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
* Time: 7:15 PM
*/

package com.github.amon.rpc

import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.channel.ChannelFutureListener

object Handler {
  def respond(request: Request, s: String) {
    val response = new DefaultHttpResponse(HTTP_1_1, OK)
    val content = ChannelBuffers.copiedBuffer(s.getBytes)
    response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
    response.setContent(content)
    val future = request.context.getChannel.write(response)
    future.addListener(ChannelFutureListener.CLOSE)
  }
}