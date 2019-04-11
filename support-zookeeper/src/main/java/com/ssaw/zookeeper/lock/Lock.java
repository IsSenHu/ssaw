package com.ssaw.zookeeper.lock;

/**
 * @author HuSen
 * @date 2019/4/11 10:05
 */
public interface Lock {

    /**
     * 加锁
     *
     * @return 加锁结果状态
     */
    State lock();

    /**
     * 解锁
     *
     * @param nodeId 节点ID
     * @return 是否解锁成功
     */
    boolean unlock(String nodeId);
}
