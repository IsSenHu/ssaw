package com.ssaw.zookeeper;

import com.ssaw.zookeeper.util.ZookeeperUtil;

/**
 * @author HuSen
 * @date 2019/3/13 14:27
 */
public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        ZookeeperUtil.watch("demo2", "/watch/test", data -> System.out.println(new String(data)));
        Thread.sleep(1000000L);
    }
}