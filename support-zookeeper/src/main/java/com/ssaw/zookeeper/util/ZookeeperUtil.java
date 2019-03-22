package com.ssaw.zookeeper.util;

import com.ssaw.zookeeper.factory.CuratorConnectFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HuSen
 * @date 2019/3/13 14:12
 */
public class ZookeeperUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperUtil.class);

    public static void watch(String namespace, String nodePath, WatchListener watchListener) {
        try {
            CuratorFramework client = CuratorConnectFactory.builder(namespace);
            ifNotExistsThenCreate(client, nodePath);
            final NodeCache nodeCache = new NodeCache(client, nodePath);
            nodeCache.start(true);
            if (nodeCache.getCurrentData() != null) {
                LOGGER.info("节点初始化数据为: {}", new String(nodeCache.getCurrentData().getData()));
            } else {
                LOGGER.info("节点初始化数据为空...");
            }
            nodeCache.getListenable().addListener(() -> watchListener.listen(nodeCache.getCurrentData().getData()));
        } catch (Exception e) {
            LOGGER.error("命名空间:{}, 节点:{} 监听失败:", namespace, nodePath, e);
        }
    }

    public static void push(String namespace, String nodePath, byte[] data) {
        try {
            CuratorFramework client = CuratorConnectFactory.builder(namespace);
            ifNotExistsThenCreate(client, nodePath);
            client.setData().forPath(nodePath, data);
        } catch (Exception e) {
            LOGGER.error("命名空间:{}, 节点:{} 推送数据失败:", namespace, nodePath, e);
        }
    }

    private static void ifNotExistsThenCreate(CuratorFramework client, String nodePath) throws Exception {
        // TODO 优化 不用每次都去查是否存在
        Stat stat = client.checkExists().forPath(nodePath);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(nodePath);
        }
    }
}