package com.ssaw.netty.echo.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author HuSen
 * @date 2019/6/5 15:33
 */
@Slf4j
public class EchoServer {
    private final int port;
    private final int portServer;

    public EchoServer(int port, int portServer) {
        this.port = port;
        this.portServer = portServer;
    }

    public void start() {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口，设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加EchoServerHandler到ChannelPipeline
                    .childHandler(new SimpleChannelInboundHandler<ByteBuffer>() {
                        ChannelFuture connectFuture;
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.channel(NioSocketChannel.class)
                                    .handler(new SimpleChannelInboundHandler<ByteBuffer>() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) {
                                            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
                                        }

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) {
                                            System.out.println("Received data");
                                            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
                                        }
                                    });
                            bootstrap.group(ctx.channel().eventLoop());
                            bootstrap.remoteAddress(new InetSocketAddress(portServer));
                            connectFuture = bootstrap.connect();
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) {
                            if (connectFuture.isDone()) {
                                System.out.println("do something with the data");
                            }
                        }
                    });
            // 异步地绑定服务器, 调用sync()方法阻塞, 等待直到绑定完成
            ChannelFuture sync = b.bind().sync();
            // 获取Channel的CloseFuture, 并且阻塞当前线程直到它完成
            sync.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt fail");
                    future.cause().printStackTrace();
                }
            });
        } catch (Exception e) {
            log.error("Echo服务异常:", e);
        }
    }
}