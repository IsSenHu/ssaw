package com.ssaw.gateway.core.callback.impl;

import com.ssaw.gateway.core.callback.CallBack;
import com.ssaw.gateway.core.pool.HttpChannelPoolMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.AllArgsConstructor;

/**
 * @author HuSen
 * create on 2019/7/1 10:45
 */
@AllArgsConstructor
public class HttpCallBack implements CallBack<Object> {

    private ChannelHandlerContext connection;

    private String key;

    @Override
    public void execute(Channel channel, Object msg) {
        if (msg instanceof HttpResponse)
        {
            HttpResponse response = (HttpResponse) msg;
            connection.write(cloneResp(response));
        }
        if(msg instanceof HttpContent)
        {
            HttpContent content = (HttpContent) msg;
            connection.write(cloneContent(content));
        }
        connection.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        HttpChannelPoolMap.getInstance().get(key).release(channel);
        HttpChannelPoolMap.removeCallBack(channel);
    }

    private HttpContent cloneContent(HttpContent content) {
        ByteBuf buf = content.content();
        return new DefaultHttpContent(buf);
    }

    private HttpResponse cloneResp(HttpResponse response) {
        HttpResponse resp = new DefaultHttpResponse(response.protocolVersion(), response.status());
        response.headers().forEach(e -> resp.headers().add(e.getKey(), e.getValue()));
        return resp;
    }
}
