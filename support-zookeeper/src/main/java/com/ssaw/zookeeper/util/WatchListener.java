package com.ssaw.zookeeper.util;

/**
 * @author HuSen
 * @date 2019/3/13 14:37
 */
@FunctionalInterface
public interface WatchListener {

    /**
     * 监听得到变化的数据
     * @param data 数据
     */
    void listen(byte[] data) throws Exception;
}
