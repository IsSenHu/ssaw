package com.ssaw.netty.echo.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author HuSen
 * @date 2019/6/11 15:23
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

	private final boolean client;

	public HttpAggregatorInitializer(boolean client) {
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		if (client) {
			pipeline.addLast("codec", new HttpClientCodec());
		} else {
			pipeline.addLast("codec", new HttpServerCodec());
		}
		// 将最大的消息设置为512KB
		pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
	}
}