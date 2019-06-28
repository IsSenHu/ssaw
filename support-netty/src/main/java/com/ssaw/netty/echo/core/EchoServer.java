package com.ssaw.netty.echo.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口，设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加EchoServerHandler到ChannelPipeline
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new SimpleChannelInboundHandler<ByteBuffer>() {
                        ChannelFuture connectFuture;
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.channel(NioSocketChannel.class)
                                    .option(ChannelOption.SO_KEEPALIVE, true)
                                    .handler(new ChannelInitializer<SocketChannel>() {

                                        @Override
                                        protected void initChannel(SocketChannel ch) {
                                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                                @Override
                                                public void channelActive(ChannelHandlerContext ctx) {
                                                    log.info("链接已建立...");
                                                    ByteBuf buffer = ctx.alloc().buffer();
                                                    buffer.writeCharSequence("1001", CharsetUtil.UTF_8);
                                                    ctx.writeAndFlush(buffer.retain());
                                                }

                                                @Override
                                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                                    System.out.println("Received data");
                                                }

                                                @Override
                                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                                    cause.printStackTrace();
                                                }
                                            });
                                        }
                                    });
                            bootstrap.group(ctx.channel().eventLoop());
                            bootstrap.remoteAddress(new InetSocketAddress(portServer));
                            connectFuture = bootstrap.connect();
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) {
                            if (connectFuture.isDone()) {
                                log.info("链接已建立...");
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeCharSequence("1001", CharsetUtil.UTF_8);
                                ctx.writeAndFlush(buffer.retain());
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