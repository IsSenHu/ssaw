package com.ssaw.gateway.core.config;

import lombok.Data;

/**
 * @author HuSen
 * create on 2019/7/2 11:34
 */
@Data
public class HttpPoolConfig {

    /**
     * 获取连接超时时间
     */
    private Long acquireTimeoutMillis = 2000L;

    /**
     * 最大连接数
     */
    private Integer maxConnections = 50;

    /**
     * 最大等待获取连接数
     */
    private Integer maxPendingAcquires = 100;

    /**
     * 释放的时候是否进行健康检查
     */

    private Boolean releaseHealthCheck = false;

    /**
     * true Channel selection will be LIFO, if false FIFO
     * true 选择 Channel 时后进先出 false 先进后出
     */
    private Boolean lastRecentUsed = false;
}
