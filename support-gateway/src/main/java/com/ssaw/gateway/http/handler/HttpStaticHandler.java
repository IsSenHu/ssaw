package com.ssaw.gateway.http.handler;

import com.ssaw.gateway.http.util.HttpUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

/**
 * @author HuSen
 * create on 2019/6/28 13:53
 */
@Slf4j
public class HttpStaticHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 处理100Continue请求以符合HTTP1.1规范
        if (is100ContinueExpected(request)) {
            HttpUtils.send100Continue(ctx);
        }
        HttpUtils.load(request, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
