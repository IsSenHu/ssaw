package com.ssaw.netty.echo.room.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author HuSen
 * @date 2019/6/11 17:57
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final ChannelGroup group;

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (((WebSocketServerProtocolHandler.ServerHandshakeStateEvent) evt).name().equals("HANDSHAKE_COMPLETE")) {
			ctx.pipeline().remove(HttpRequestHandler.class);
			group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + "joined"));
			group.add(ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		group.writeAndFlush(msg.retain());
	}
}