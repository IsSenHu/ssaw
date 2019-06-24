package com.ssaw.netty.echo.room;

import com.ssaw.netty.echo.room.server.ChatServer;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/24 13:24
 */
public class Main {
    public static void main(String[] args) {
        final ChatServer endpoint = new ChatServer();
        ChannelFuture future = endpoint.start(new InetSocketAddress(8888));
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
