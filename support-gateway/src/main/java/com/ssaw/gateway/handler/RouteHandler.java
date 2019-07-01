package com.ssaw.gateway.handler;

import com.ssaw.gateway.core.client.HttpClient;
import com.ssaw.gateway.core.entity.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2019/7/1 14:38
 */
@Slf4j
public class RouteHandler extends SimpleChannelInboundHandler<Request> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("channelActive: {}", ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) {
        HttpClient.getInstance().get(msg.getKey(), msg.getRemain(), ctx, msg.getRequest());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("异常 ", cause);
        ctx.close();
    }
}
