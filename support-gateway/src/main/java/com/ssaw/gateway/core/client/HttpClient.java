package com.ssaw.gateway.core.client;

import com.ssaw.gateway.core.callback.impl.HttpCallBack;
import com.ssaw.gateway.core.pool.HttpChannelPoolMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Http 客户端
 *
 * @author HuSen
 * create on 2019/7/1 11:29
 */
@Slf4j
public class HttpClient {

    /**
     * 私有化构造方法
     */
    private HttpClient() {}

    private static final HttpChannelPoolMap POOL_MAP = HttpChannelPoolMap.getInstance();

    private static final HttpClient INSTANCE = new HttpClient();

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("proxy");

    private static final String KEY_SUFFIX = ".key";
    private static final String HOST_SUFFIX = ".host";
    private static final String PORT_SUFFIX = ".port";

    static
    {
        Set<String> keySet = BUNDLE.keySet();
        // TODO 用config来代替
        Set<String> servers = keySet.stream().map(k -> StringUtils.substringBefore(k, ".")).collect(Collectors.toSet());
        servers.forEach(s -> POOL_MAP.build(BUNDLE.getString(s.concat(KEY_SUFFIX)), BUNDLE.getString(s.concat(HOST_SUFFIX)), Integer.valueOf(BUNDLE.getString(s.concat(PORT_SUFFIX)))));
        POOL_MAP.buildFinish();
    }

    public static HttpClient getInstance() {
        return INSTANCE;
    }

    public void get(String key, String uri, ChannelHandlerContext connection, FullHttpRequest request) {
        FixedChannelPool pool = POOL_MAP.get(key);
        if (null == pool) {
            return;
        }
        Future<Channel> future = pool.acquire();
        future.addListener((FutureListener<Channel>) f -> {
            Channel channel = f.getNow();
            HttpRequest req = new DefaultHttpRequest(request.protocolVersion(), request.method(), uri);
            HttpCallBack httpCallBack = new HttpCallBack(connection, key);
            POOL_MAP.addCallBack(channel, httpCallBack);
            channel.write(req);
            channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        });
    }

    /**
     * 这个方法是为了让该类去加载
     */
    public void load() {}
}
