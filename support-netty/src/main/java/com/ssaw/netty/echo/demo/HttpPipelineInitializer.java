package com.ssaw.netty.echo.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * @author HuSen
 * @date 2019/6/11 15:15
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

	private final boolean client;

	public HttpPipelineInitializer(boolean client) {
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		if (client) {
			// 入站响应 解码
			pipeline.addLast("decoder", new HttpResponseDecoder());
			// 出站请求 编码
			pipeline.addLast("encoder", new HttpRequestDecoder());
		} else {
			// 入站请求 解码
			pipeline.addLast("decoder", new HttpRequestDecoder());
			// 出站响应 编码
			pipeline.addLast("encoder", new HttpResponseDecoder());
		}
	}
}