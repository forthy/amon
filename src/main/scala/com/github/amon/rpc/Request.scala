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

import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.channel.ChannelHandlerContext

case class Request(req: HttpRequest, context: ChannelHandlerContext, content: Array[Byte]) {
  def uri = req.getUri

  def method = req.getMethod.getName

  def isExternal = req.getHeader("mode") == EXTERNAL.name

  def getMode = if (isExternal) EXTERNAL else INTERNAL
}

case class Mode(name: String)

/**
 * External request is sent from an external API client and may need replication like put, delete etc
 */

object EXTERNAL extends Mode("EXTERNAL")

/**
 * Internal request is sent internally from a node
 */

object INTERNAL extends Mode("INTERNAL")