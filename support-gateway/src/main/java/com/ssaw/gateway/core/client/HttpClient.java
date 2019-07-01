package com.ssaw.gateway.core.client;

import com.ssaw.gateway.core.pool.HttpChannelPoolMap;
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
public class HttpClient {

    /**
     * 私有化构造方法
     */
    private HttpClient() {}

    private static final HttpChannelPoolMap POOL_MAP = new HttpChannelPoolMap();

    private static final HttpClient INSTANCE = new HttpClient();

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("proxy.properties");

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
}
