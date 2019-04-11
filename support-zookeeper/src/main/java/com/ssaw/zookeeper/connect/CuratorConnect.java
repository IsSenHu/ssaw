package com.ssaw.zookeeper.connect;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author HuSen
 * @date 2019/3/13 10:58
 */
public class CuratorConnect {
    /** zk地址，集群模式则是多个ip */
    private static final String ZK_SERVER_IPS = "118.24.38.46:2181";

    public CuratorFrameworkFactory.Builder baseBuild() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        return CuratorFrameworkFactory.builder()
                .connectString(ZK_SERVER_IPS)
                .sessionTimeoutMs(15000)
                .retryPolicy(retryPolicy);
    }
}