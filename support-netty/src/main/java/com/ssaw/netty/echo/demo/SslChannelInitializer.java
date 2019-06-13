package com.ssaw.netty.echo.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author HuSen
 * @date 2019/6/11 15:03
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

	private final SslContext context;
	private final boolean startTls;

	public SslChannelInitializer(SslContext context, boolean startTls) {
		this.context = context;
		this.startTls = startTls;
	}

	@Override
	protected void initChannel(Channel ch) {
		SSLEngine sslEngine = context.newEngine(ch.alloc());
		ch.pipeline().addFirst("ssl", new SslHandler(sslEngine, startTls));
	}
}