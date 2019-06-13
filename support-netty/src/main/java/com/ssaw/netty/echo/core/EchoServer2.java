package com.ssaw.netty.echo.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author HuSen
 * @date 2019/6/5 15:33
 */
@Slf4j
public class EchoServer2 {
    private final int port;

    public EchoServer2(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口，设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加EchoServerHandler到ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步地绑定服务器, 调用sync()方法阻塞, 等待直到绑定完成
            ChannelFuture sync = b.bind().sync();
            // 获取Channel的CloseFuture, 并且阻塞当前线程直到它完成
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Echo服务异常:", e);
        } finally {
            // 关闭EventLoopGroup释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}