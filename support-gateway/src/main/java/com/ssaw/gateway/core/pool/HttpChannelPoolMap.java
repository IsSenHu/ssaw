package com.ssaw.gateway.core.pool;

import com.ssaw.gateway.core.callback.CallBack;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Http连接池
 *
 * @author HuSen
 * create on 2019/7/1 10:26
 */
@Slf4j
public class HttpChannelPoolMap extends AbstractChannelPoolMap<String, FixedChannelPool> {

    private static final Map<Channel, CallBack<Object>> CHANNEL_CALL_BACK_MAPPING = new HashMap<>(1000);

    private static final HttpChannelPoolMap INSTANCE = new HttpChannelPoolMap();

    /**
     * 私有构造方法
     */
    private HttpChannelPoolMap() {}

    public static HttpChannelPoolMap getInstance() {
        return INSTANCE;
    }

    public static void removeCallBack(Channel channel) {
        CHANNEL_CALL_BACK_MAPPING.remove(channel);
    }

    public void addCallBack(Channel channel, CallBack<Object> callBack) {
        CHANNEL_CALL_BACK_MAPPING.put(channel, callBack);
    }

    private static final Map<String, HttpChannelPoolHandler> KEY_HANDLER_MAPPING = new HashMap<>(6);

    @Override
    protected FixedChannelPool newPool(String key) {
        HttpChannelPoolHandler handler = KEY_HANDLER_MAPPING.get(key);
        if (null != handler) {
            return new FixedChannelPool(
                    // 引导
                    handler.bootstrap,
                    // ChannelPoolHandler
                    handler,
                    // Channel检查
                    ChannelHealthChecker.ACTIVE,
                    // 获取连接超时处理
                    FixedChannelPool.AcquireTimeoutAction.FAIL,
                    // 获取连接超时时间
                    2000,
                    // 最大连接数
                    50,
                    // 最大等待获取连接数
                    1000,
                    // 释放的时候是否进行健康检查
                    false,
                    // true Channel selection will be LIFO, if false FIFO
                    // true 选择 Channel 时后进先出 false 先进后出
                    false
            );
        }
        return null;
    }

    /**
     * Http 的 ChannelPoolHandler
     */
    private class HttpChannelPoolHandler implements ChannelPoolHandler {

        private final Bootstrap bootstrap = new Bootstrap();

        private String id;
        private String host;
        private Integer port;

        HttpChannelPoolHandler(String id, String host, Integer port) {
            this.id = id;
            this.host = host;
            this.port = port;
            EventLoopGroup group = new NioEventLoopGroup();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                    .remoteAddress(new InetSocketAddress(host, port));
        }


        /**
         * 使用完channel需要释放才能放入连接池
         *
         * @param ch Channel
         */
        @Override
        public void channelReleased(Channel ch) {
            // flush掉所有写回的数据
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER);
            log.info("{} channelReleased......", ch);
        }

        /**
         * 获取连接池中的channel
         *
         * @param ch Channel
         */
        @Override
        public void channelAcquired(Channel ch) {
            log.info("{} channelAcquired......", ch);
        }

        /**
         * 当链接创建的时候添加channel handler，只有当channel不足时会创建，但不会超过限制的最大channel数
         *
         * @param ch Channel
         */
        @Override
        public void channelCreated(Channel ch) {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
            pipeline.addLast(new ChannelInboundHandlerAdapter() {

                @Override
                public void channelActive(ChannelHandlerContext ctx) {
                    // 在这里完成连接建立
                    log.info("已成功连接到id:{}, host:{}, port:{}", id, host, port);
                }

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                    // 在这里可以读取到响应数据
                    CHANNEL_CALL_BACK_MAPPING.get(ctx.channel()).execute(ctx.channel(), msg);
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                    // 这里处理异常
                    log.error("路由到远程服务异常: ", cause);
                }

                @Override
                public void channelReadComplete(ChannelHandlerContext ctx) {
                    log.info("响应数据完成");
                }
            });
        }
    }

    /**
     * 构建 pool 的所有远程服务
     *
     * @param key  远程服务标识
     * @param host host
     * @param port port
     */
    public void build(String key, String host, Integer port) {
        HttpChannelPoolHandler handler = new HttpChannelPoolHandler(key, host, port);
        if (!KEY_HANDLER_MAPPING.containsKey(key)) {
            KEY_HANDLER_MAPPING.put(key, handler);
        } else {
            throw new IllegalArgumentException(key + " is existed");
        }
    }

    /**
     * 构建完成
     */
    public void buildFinish() {
        KEY_HANDLER_MAPPING.forEach((key, value) -> newPool(key));
    }
}
