package com.ssaw.netty.echo.room.initializer;

import com.ssaw.netty.echo.room.handler.HttpRequestHandler;
import com.ssaw.netty.echo.room.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author HuSen
 * create on 2019/6/24 13:04
 */
public class ChannelServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ChannelServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        // 将字节码解码为HTTPRequest、HTTPContent、HTTPLastContent。并将HTTPRequest、HTTPContent、HTTPLastContent编码为字节
        pipeline.addLast(new HttpServerCodec())
                // 写入一个文件的类容
                .addLast(new ChunkedWriteHandler())
                // 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合
                // 为单个 FullHttpRequest 或者 FullHttpResponse（取
                // 决于它是被用来处理请求还是响应）。安装了这个之后，
                // ChannelPipeline 中的下一个 ChannelHandler 将只会
                // 收到完整的 HTTP 请求或响应
                .addLast(new HttpObjectAggregator(64 * 1024))
                // 处理 FullHttpRequest（那些不发送到/ws URI 的请求）
                .addLast(new HttpRequestHandler("/ws"))
                // 按照 WebSocket 规范的要求，处理 WebSocket 升级握手、
                // PingWebSocketFrame 、 PongWebSocketFrame 和
                // CloseWebSocketFrame
                .addLast(new WebSocketServerProtocolHandler("/ws"))
                // 处理 TextWebSocketFrame 和握手完成事件
                .addLast(new TextWebSocketFrameHandler(group));
    }
}
