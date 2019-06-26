package com.ssaw.netty.echo.udp.broadcaster;

import com.ssaw.netty.echo.udp.encoder.LogEventEncoder;
import com.ssaw.netty.echo.udp.pojo.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 18:26
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception {
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            } else if (len > pointer) {
                // content was added
                RandomAccessFile raf = new RandomAccessFile(this.file, "r");
                raf.seek(pointer);
                // 设置当前文件的指针，以确保没有任何的旧日志被发送
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, file.getAbsolutePath(), line, -1L));
                }
                // 存储其在文件中的当前位置
                pointer = raf.getFilePointer();
                raf.close();
            }
            // 休眠一秒，如果被中断，则退出循环；否则重新处理它
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
