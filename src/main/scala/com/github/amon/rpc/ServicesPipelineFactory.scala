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
* Time: 3:03 PM
*/

package com.github.amon.rpc

import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.handler.codec.http.{HttpResponseEncoder, HttpChunkAggregator, HttpRequestDecoder}
import org.jboss.netty.handler.stream.ChunkedWriteHandler

class ServicesPipelineFactory(handler: Request => Response) extends ChannelPipelineFactory {

  def getPipeline = {
    val pipeline = org.jboss.netty.channel.Channels.pipeline()

    // Uncomment the following line if you want HTTPS
    //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
    //engine.setUseClientMode(false);
    //pipeline.addLast("ssl", new SslHandler(engine));

    pipeline.addLast("decoder", new HttpRequestDecoder())
    pipeline.addLast("aggregator", new HttpChunkAggregator(65536))
    pipeline.addLast("encoder", new HttpResponseEncoder())
    pipeline.addLast("chunkedWriter", new ChunkedWriteHandler())
    pipeline.addLast("handler", new ServicesHandler(handler))

    pipeline
  }

}