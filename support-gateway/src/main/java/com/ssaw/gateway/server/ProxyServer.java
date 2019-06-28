package com.ssaw.gateway.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuSen
 * create on 2019/6/28 16:41
 */
@Slf4j
@Data
public class ProxyServer {

    /**
     * ChannelHandlerContext
     */
    private ChannelHandlerContext connection;

    private static final AttributeKey<Integer> KEY_CHANNEL = AttributeKey.valueOf("KEY_CHANNEL");

    private static final Map<Integer, ChannelHandlerContext> CONTEXT_MAP = new ConcurrentHashMap<>();

    private Bootstrap bootstrap;

    private NioEventLoopGroup group;

    private String id;

    private String host;

    private Integer port;

    private AtomicInteger atomicInteger = new AtomicInteger();

    public ProxyServer(String id, String host, Integer port) {
        this.id = id;
        this.host = host;
        this.port = port;
        group = new NioEventLoopGroup();
    }

    public void start() {
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(createInitializer());
        bootstrap.connect().syncUninterruptibly();
    }

    private ChannelHandler createInitializer() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpClientCodec())
                        .addLast(new ChunkedWriteHandler())
                        .addLast(new HttpObjectAggregator(1024 * 64))
                        .addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                // 在这里完成连接建立
                                log.info("已成功连接到id:{}, host:{}, port:{}", id, host, port);
                                connection = ctx;
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                // 在这里可以读取到响应数据
                                DefaultHttpResponse ret = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                Integer integer = ctx.channel().attr(KEY_CHANNEL).get();
                                log.info("now {}", integer);
                                if (msg instanceof HttpResponse)
                                {
                                    HttpResponse response = (HttpResponse) msg;
                                    response.headers().entries().forEach(e -> ret.headers().add(e.getKey(), e.getValue()));

                                }
                                DefaultHttpContent c = null;
                                if(msg instanceof HttpContent)
                                {
                                    HttpContent content = (HttpContent)msg;
                                    ByteBuf buf = content.content();
                                    c = new DefaultHttpContent(buf);
                                    System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
                                }
                                ChannelHandlerContext context = CONTEXT_MAP.get(integer);
                                context.write(ret);
                                if (c != null) {
                                    context.write(c);
                                }
                                context.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                // 这里处理异常
                                log.error("路由到远程服务异常: ", cause);
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) {
                                log.info("响应数据完成");
                                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                                        .addListener(ChannelFutureListener.CLOSE);
                            }
                        });
            }
        };
    }

    public void close() {
        group.shutdownGracefully();
    }

    public void request(String uri, HttpMethod method, ChannelHandlerContext clientConnection) {
        int key = atomicInteger.getAndIncrement();
        CONTEXT_MAP.put(key, clientConnection);
        connection.channel().attr(KEY_CHANNEL).set(key);
        DefaultHttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, uri);
        connection.writeAndFlush(request);
    }
}
