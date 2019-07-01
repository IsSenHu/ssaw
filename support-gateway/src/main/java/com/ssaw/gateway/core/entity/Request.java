package com.ssaw.gateway.core.entity;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2019/7/1 14:34
 */
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = -5362296566080468672L;
    private FullHttpRequest request;
    private String key;
    /**
     * 剩余请求路径
     */
    private String remain;

}
