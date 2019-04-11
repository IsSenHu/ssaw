package com.ssaw.zookeeper.lock;

import com.ssaw.zookeeper.factory.CuratorConnectFactory;
import com.ssaw.zookeeper.util.ZookeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author HuSen
 * @date 2019/4/10 16:29
 */
@Slf4j
public class ZookeeperExclusiveLock implements Lock {

    private CuratorFramework curatorFramework;

    private static volatile ConcurrentMap<String, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();

    private static final String T = "/t";

    private static final String PATH_PREFIX = "/";

    private String lockType;

    public ZookeeperExclusiveLock(String lockType) {
        this.lockType = lockType;
        this.curatorFramework = CuratorConnectFactory.builder(lockType);
    }

    @Override
    public State lock() {
        try {
            // 创建节点
            String nodeId = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(T);
            // 获取所有的锁
            TreeSet<String> allNode = new TreeSet<>(curatorFramework.getChildren().forPath("/"));
            Map<Long, String> table = new TreeMap<>();
            for (String node : allNode) {
                Long seq = Long.valueOf(node.replace(T, "").replace("t", ""));
                table.put(seq, node);
            }
            // 如果当前创建的序列最小 则获得锁
            String smallest = allNode.first();
            if (!(PATH_PREFIX + smallest).equals(nodeId)) {
                // 否则监听该节点的前一个节点
                Long seq = Long.valueOf(nodeId.replace(T, ""));
                String beforeNode = table.get(seq - 1);
                ZookeeperUtil.watch(lockType, PATH_PREFIX + beforeNode, data -> curatorFramework.notifyAll());
                synchronized (ZookeeperExclusiveLock.class) {
                    curatorFramework.wait();
                }
            }
            return new State(true, nodeId);
        } catch (Exception e) {
            log.error("lock fail:", e);
            return new State(false, null);
        }
    }

    @Override
    public boolean unlock(String nodeId) {
        try {
            curatorFramework.delete().forPath(nodeId);
            return true;
        } catch (Exception e) {
            log.error("unlock fail:", e);
            return false;
        }
    }
}