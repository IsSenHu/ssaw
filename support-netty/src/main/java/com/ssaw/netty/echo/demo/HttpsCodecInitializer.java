package com.ssaw.netty.echo.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author HuSen
 * @date 2019/6/11 15:31
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

	private final SslContext context;
	private final boolean client;

	public HttpsCodecInitializer(SslContext context, boolean client) {
		this.context = context;
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine engine = context.newEngine(ch.alloc());
		pipeline.addFirst("ssl", new SslHandler(engine));
		if (client) {
			pipeline.addLast("codec", new HttpClientCodec());
		} else {
			pipeline.addLast("codec", new HttpServerCodec());
		}
	}
}