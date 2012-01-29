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
import org.jboss.netty.handler.codec.http.{HttpRequest}

class ServicesHandler(handler: Request => Any) extends SimpleChannelUpstreamHandler with Logging {

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
    e.getMessage match {
      case request: HttpRequest => {
        handler(Request(request, ctx))
      }
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    warn("Unexpected exception from downstream %s".format(e.getCause))
    e.getChannel.close()
  }
}

