package com.ssaw.gateway.handler;

import com.ssaw.gateway.core.entity.Request;
import com.ssaw.gateway.core.pool.HttpChannelPoolMap;
import com.ssaw.gateway.http.util.HttpUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

/**
 * @author HuSen
 * create on 2019/6/28 15:54
 */
public class RequestToTargetHandler extends MessageToMessageDecoder<FullHttpRequest> {

    private static final String PREFIX = "/";

    private static final String SPLIT = PREFIX;

    private static final String FAVICON = "/favicon.ico";

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> out) {
        // 处理100Continue请求以符合HTTP1.1规范
        if (is100ContinueExpected(request)) {
            HttpUtils.send100Continue(ctx);
        }
        String uri = request.uri();
        // 获取路由目的地标识
        String temp = StringUtils.substringAfter(uri, PREFIX);
        String targetId = StringUtils.contains(temp, SPLIT) ? StringUtils.substringBefore(temp, SPLIT) : temp;
        // 没有路由的目的地
        if (StringUtils.equals(uri, PREFIX) || !HttpChannelPoolMap.getInstance().exist(targetId)) {
            HttpUtils.returnStatus("this route is not found", HttpResponseStatus.NOT_FOUND, request, ctx);
        }
        else if (StringUtils.equals(uri, FAVICON)) {
            HttpUtils.load(request, ctx);
        } else {
            // 获取剩余的路径
            String remain = StringUtils.substringAfter(uri, targetId);
            Request req = new Request();
            req.setRequest(request);
            req.setKey(targetId);
            req.setRemain(remain);
            out.add(req);
        }
    }
}
