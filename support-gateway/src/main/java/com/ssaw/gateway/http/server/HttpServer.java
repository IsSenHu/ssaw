package com.ssaw.gateway.http.server;

import com.ssaw.gateway.http.handler.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/28 13:44
 */
public class HttpServer {
    private final int port;
    private final NioEventLoopGroup group;

    public HttpServer(int port) {
        this.port = port;
        group = new NioEventLoopGroup();
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.localAddress(new InetSocketAddress(port));
        bootstrap.childHandler(createInitializer());
        bootstrap.bind().syncUninterruptibly();
    }

    private ChannelHandler createInitializer() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                // 将字节码解码为HTTPRequest、HTTPContent、HTTPLastContent。并将HTTPRequest、HTTPContent、HTTPLastContent编码为字节
                pipeline.addLast(new HttpServerCodec());
                // 写入一个文件的类容
                pipeline.addLast(new ChunkedWriteHandler());
                // 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合
                // 为单个 FullHttpRequest 或者 FullHttpResponse（取
                // 决于它是被用来处理请求还是响应）。安装了这个之后，
                // ChannelPipeline 中的下一个 ChannelHandler 将只会
                // 收到完整的 HTTP 请求或响应
                pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                pipeline.addLast(new HttpRequestHandler());
            }
        };
    }
}
