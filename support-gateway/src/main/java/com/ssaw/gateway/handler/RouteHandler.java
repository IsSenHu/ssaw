package com.ssaw.gateway.handler;

import com.ssaw.gateway.core.entity.TargetServer;
import com.ssaw.gateway.server.ProxyServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HuSen
 * create on 2019/6/28 15:49
 */
@Slf4j
@ChannelHandler.Sharable
public class RouteHandler extends SimpleChannelInboundHandler<TargetServer> {

    private static final ConcurrentMap<String, ProxyServer> TARGET_ID_MAPPING = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("channel {} 已就绪", ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TargetServer targetServer) {
        ProxyServer proxyServer = TARGET_ID_MAPPING.get(targetServer.getId());
        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ctx.channel().writeAndFlush(response);
//        proxyServer.request(targetServer.getRemain(), targetServer.getHttpMethod(), ctx);
    }

    public static void addProxy(ProxyServer proxyServer) {
        TARGET_ID_MAPPING.put(proxyServer.getId(), proxyServer);
    }
}
