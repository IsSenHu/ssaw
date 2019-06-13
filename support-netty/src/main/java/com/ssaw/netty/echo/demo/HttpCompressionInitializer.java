package com.ssaw.netty.echo.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author HuSen
 * @date 2019/6/11 15:27
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

	private final boolean client;

	public HttpCompressionInitializer(boolean client) {
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		if (client) {
			pipeline.addLast("codec", new HttpClientCodec())
					.addLast("decompressor", new HttpContentDecompressor());
		} else {
			pipeline.addLast("codec", new HttpServerCodec())
					.addLast("compressor", new HttpContentCompressor());
		}
	}
}