package com.ssaw.zookeeper.factory;

import com.ssaw.zookeeper.connect.CuratorConnect;
import org.apache.curator.framework.CuratorFramework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuSen
 * @date 2019/3/13 13:57
 */
public class CuratorConnectFactory {

    private static final Map<String, CuratorFramework> CURATOR_FRAMEWORK_MAP = new ConcurrentHashMap<>();

    public static CuratorFramework builder(String namespace) {
        CuratorFramework curatorFramework = CURATOR_FRAMEWORK_MAP.get(namespace);
        if (curatorFramework == null) {
            synchronized (CuratorConnectFactory.class) {
                if (CURATOR_FRAMEWORK_MAP.get(namespace) == null) {
                    curatorFramework = new CuratorConnect().baseBuild().namespace(namespace).build();
                    curatorFramework.start();
                    CURATOR_FRAMEWORK_MAP.put(namespace, curatorFramework);
                }
            }
        }
        return CURATOR_FRAMEWORK_MAP.get(namespace);
    }

    public static void close(String namespace) {
        CuratorFramework curatorFramework = CURATOR_FRAMEWORK_MAP.get(namespace);
        if (null != curatorFramework) {
            curatorFramework.close();
            CURATOR_FRAMEWORK_MAP.remove(namespace);
        }
    }
}