package com.ssaw.netty.echo.udp;

import com.ssaw.netty.echo.udp.broadcaster.LogEventBroadcaster;
import com.ssaw.netty.echo.udp.monitor.LogEventMonitor;
import io.netty.channel.Channel;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 18:38
 */
public class Main {

    private static class Broadcaster {
        public static void main(String[] args) {
            LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", 6666), new File("D:\\my\\ssaw\\support-netty\\src\\main\\resource\\debug_log.log"));
            try {
                broadcaster.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                broadcaster.stop();
            }
        }
    }

    private static class Monitor {
        public static void main(String[] args) throws InterruptedException {
            LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(6666));
            try {
                Channel channel = monitor.bind();
                channel.closeFuture().sync();
            } finally {
                monitor.stop();
            }
        }
    }
}
