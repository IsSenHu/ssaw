package com.ssaw.netty.echo.udp;

import com.ssaw.netty.echo.udp.broadcaster.LogEventBroadcaster;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 18:38
 */
public class Main {
    public static void main(String[] args) throws Exception {
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", 6666), new File("D:\\my\\ssaw\\support-netty\\src\\main\\resource\\index.html"));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
