package com.ssaw.zookeeper;

import com.ssaw.zookeeper.factory.CuratorConnectFactory;
import org.apache.curator.framework.CuratorFramework;

/**
 * @author HuSen
 * @date 2019/4/16 11:25
 */
public class Demo3 {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorConnectFactory
                .builder("testLock");
        String forPath = curatorFramework.create().creatingParentsIfNeeded().forPath("/nihao");
        System.out.println(forPath);
    }
}