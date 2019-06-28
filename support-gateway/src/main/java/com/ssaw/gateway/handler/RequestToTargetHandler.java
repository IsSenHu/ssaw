package com.ssaw.gateway.handler;

import com.ssaw.gateway.core.entity.TargetServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HuSen
 * create on 2019/6/28 15:54
 */
public class RequestToTargetHandler extends MessageToMessageDecoder<FullHttpRequest> {

    private static final ConcurrentMap<String, TargetServer> TARGET_ID_MAPPING = new ConcurrentHashMap<>();

    private static final String PREFIX = "/";

    private static final String SPLIT = PREFIX;

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> out) {
        String uri = request.uri();
        // 没有路由的目的地
        if (StringUtils.equals(uri, PREFIX)) {
            return;
        }
        // 获取路由目的地标识
        String temp = StringUtils.substringAfter(uri, PREFIX);
        String targetId = StringUtils.contains(temp, SPLIT) ? StringUtils.substringBefore(temp, SPLIT) : temp;
        // 获取路由目的地
        TargetServer targetServer = TARGET_ID_MAPPING.get(targetId);
        // 如果 targetServer 为空 尝试连接
        if (null != targetServer) {
            TargetServer copy = targetServer.deepClone();
            // 获取剩余的路径
            String remain = StringUtils.substringAfter(uri, targetId);
            copy.setRemain(remain);
            copy.setHttpMethod(request.method());
            out.add(copy);
        }
    }

    public static void addTarget(TargetServer targetServer) {
        TARGET_ID_MAPPING.put(targetServer.getId(), targetServer);
    }
}
