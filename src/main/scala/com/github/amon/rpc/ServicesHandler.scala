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
* Date: 1/28/12
* Time: 3:04 PM
*/

package com.github.amon.rpc

import org.jboss.netty.channel._
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.handler.codec.http.{HttpChunk, DefaultHttpResponse, HttpRequest}
import java.io.ByteArrayOutputStream
import org.jboss.netty.buffer.{ChannelBuffers}

class ServicesHandler(handler: Request => Response) extends SimpleChannelUpstreamHandler with Logging {

  var readingChunks = false
  val buf = new ByteArrayOutputStream()

  override def messageReceived(ctx: ChannelHandlerContext, event: MessageEvent) {
    if (!readingChunks) {
      event.getMessage match {
        case request: HttpRequest =>
          if (request.isChunked)
            readingChunks = true
          else
            writeResponse(event, handler(Request(request, ctx, request.getContent.array())))
      }
    } else {
      event.getMessage match {
        case chunk: HttpChunk =>
          if (chunk.isLast) {
            readingChunks = false
            writeResponse(event, handler(Request(event.getMessage.asInstanceOf[HttpRequest], ctx, buf.toByteArray)))
          } else buf.write(chunk.getContent.array())
      }
    }
  }

  private def writeResponse(event: MessageEvent, rs: Response) {
    val content = ChannelBuffers.copiedBuffer(rs.content)
    val response = new DefaultHttpResponse(HTTP_1_1, OK)
    response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
    response.setContent(content)
    val future = event.getChannel.write(response)
    future.addListener(ChannelFutureListener.CLOSE)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    warn("Unexpected exception from downstream: %s".format(e.getCause))
    e.getChannel.close()
  }
}

