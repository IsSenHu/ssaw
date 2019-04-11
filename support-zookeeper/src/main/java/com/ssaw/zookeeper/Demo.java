package com.ssaw.zookeeper;

import com.ssaw.zookeeper.factory.ZookeeperLockFactory;
import com.ssaw.zookeeper.lock.Lock;
import com.ssaw.zookeeper.lock.State;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @date 2019/3/13 10:57
 */
public class Demo {

    static int i = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 200L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
            final Lock lock = ZookeeperLockFactory.of(ZookeeperLockFactory.LockType.EXCLUSIVE, "me-hu-sen");
            Assert.notNull(lock, "lock not allowed null");
            for (int i = 0; i < 1000; i++) {
                threadPoolExecutor.execute(() -> {
                    String nodeId = null;
                    try {
                        State state = lock.lock();
                        if (state.isLocked()) {
                            nodeId = state.getNodeId();
                        }
                        System.out.println(++Demo.i);
                    } finally {
                        if (Objects.nonNull(nodeId)) {
                            lock.unlock(nodeId);
                        }
                    }
                });
            }
        }).start();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 200L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
        final Lock lock = ZookeeperLockFactory.of(ZookeeperLockFactory.LockType.EXCLUSIVE, "me-hu-sen");
        Assert.notNull(lock, "lock not allowed null");
        for (int i = 0; i < 1000; i++) {
            threadPoolExecutor.execute(() -> {
                String nodeId = null;
                try {
                    State state = lock.lock();
                    if (state.isLocked()) {
                        nodeId = state.getNodeId();
                    }
                    System.out.println(++Demo.i);
                } finally {
                    if (Objects.nonNull(nodeId)) {
                        lock.unlock(nodeId);
                    }
                }
            });
        }

    }
}