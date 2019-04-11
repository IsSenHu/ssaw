package com.ssaw.zookeeper.factory;

import com.ssaw.zookeeper.lock.Lock;
import com.ssaw.zookeeper.lock.ZookeeperExclusiveLock;
import lombok.Getter;

/**
 * @author HuSen
 * @date 2019/4/11 10:01
 */
public class ZookeeperLockFactory {

    public static Lock of(LockType lockType, String lockObject) {
        switch (lockType) {
            case EXCLUSIVE: {
                return new ZookeeperExclusiveLock(lockObject);
            }
            case READ_WRITE: {
                return null;
            }
            default: return null;
        }
    }

    @Getter
    public static enum LockType {
        // 排它锁
        EXCLUSIVE,
        // 读写锁
        READ_WRITE
    }
}