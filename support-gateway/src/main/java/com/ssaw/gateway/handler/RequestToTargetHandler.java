package com.ssaw.gateway.handler;

import com.ssaw.gateway.core.entity.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        String uri = request.uri();
        // 没有路由的目的地
        if (StringUtils.equals(uri, PREFIX)) {
            return;
        }
        if (StringUtils.equals(uri, FAVICON)) {
            return;
        }
        // 获取路由目的地标识
        String temp = StringUtils.substringAfter(uri, PREFIX);
        String targetId = StringUtils.contains(temp, SPLIT) ? StringUtils.substringBefore(temp, SPLIT) : temp;
        // 获取路由目的地
        // 获取剩余的路径
        String remain = StringUtils.substringAfter(uri, targetId);
        Request req = new Request();
        req.setRequest(request);
        req.setKey(targetId);
        req.setRemain(remain);
        out.add(req);
    }
}
