package com.ssaw.zookeeper;

import com.ssaw.zookeeper.factory.CuratorConnectFactory;
import com.ssaw.zookeeper.util.ZookeeperUtil;

import java.util.UUID;

/**
 * @author HuSen
 * @date 2019/3/13 10:57
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        while (true) {
            Thread.sleep(1000);
            ZookeeperUtil.push("demo2", "/watch/test", UUID.randomUUID().toString().getBytes());
            CuratorConnectFactory.close("demo2");
        }
    }
}