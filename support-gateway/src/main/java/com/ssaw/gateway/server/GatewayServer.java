package com.ssaw.gateway.server;

import com.ssaw.gateway.handler.RequestToTargetHandler;
import com.ssaw.gateway.handler.RouteHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * 网关服务
 *
 * @author HuSen
 * create on 2019/6/28 15:03
 */
public class GatewayServer {
    private final int port;
    private final NioEventLoopGroup group;

    /**
     * 服务器致力于使用一个父 Channel 来接受来自客户端的连接，并创建子 Channel 以用于它们之间的通信；
     *
     * @param port 端口
     */
    public GatewayServer(int port) {
        this.port = port;
        group = new NioEventLoopGroup();
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(createInitializer());
        bootstrap.bind().syncUninterruptibly();
        
    }

    private ChannelHandler createInitializer() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpServerCodec())
                        .addLast(new HttpObjectAggregator(1024 * 64))
                        .addLast(new RequestToTargetHandler())
                        .addLast(new RouteHandler());
            }
        };
    }

    public void close() {
        group.shutdownGracefully();
    }
}
