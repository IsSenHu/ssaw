package com.ssaw.zookeeper.util;

import com.ssaw.zookeeper.factory.CuratorConnectFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/3/13 14:12
 */
public class ZookeeperUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperUtil.class);

    private static Set<String> namespaceAndNodePathCache = new HashSet<>();

    public static void watch(String namespace, String nodePath, WatchListener watchListener) {
        try {
            CuratorFramework client = CuratorConnectFactory.builder(namespace);
            preCheck(namespace, nodePath, client);
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
            preCheck(namespace, nodePath, client);
            client.setData().forPath(nodePath, data);
        } catch (Exception e) {
            LOGGER.error("命名空间:{}, 节点:{} 推送数据失败:", namespace, nodePath, e);
        }
    }

    private static void preCheck(String namespace, String nodePath, CuratorFramework client) throws Exception {
        final String line = "-";
        String unique = namespace.concat(line).concat(nodePath);
        if (!namespaceAndNodePathCache.contains(unique)) {
            ifNotExistsThenCreate(client, nodePath);
            namespaceAndNodePathCache.add(unique);
        }
    }

    private static void ifNotExistsThenCreate(CuratorFramework client, String nodePath) throws Exception {
        Stat stat = client.checkExists().forPath(nodePath);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(nodePath);
        }
    }
}