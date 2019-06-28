package com.ssaw.gateway.core.entity;

import io.netty.handler.codec.http.HttpMethod;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2019/6/28 16:04
 */
@Data
public class TargetServer implements Serializable {
    private static final long serialVersionUID = 1356466763543286024L;

    private static final String SPLIT_WITH_HP = ":";

    /**
     * 服务ID
     */
    private String id;

    /**
     * 主机Host
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 剩余请求路径
     */
    private String remain;

    /**
     * HTTP 请求方法
     */
    private HttpMethod httpMethod;

    /**
     * 克隆
     *
     * @return 本身的一个深度拷贝
     */
    public TargetServer deepClone() {
        TargetServer targetServer = new TargetServer();
        targetServer.setHost(host);
        targetServer.setId(id);
        targetServer.setPort(port);
        targetServer.setRemain(remain);
        targetServer.setHttpMethod(httpMethod);
        return targetServer;
    }

    /**
     * 返回请求真实uri
     *
     * @return 请求真实uri
     */
    public String uri() {
        return host.concat(SPLIT_WITH_HP).concat(port.toString()).concat(StringUtils.trim(remain));
    }
}
