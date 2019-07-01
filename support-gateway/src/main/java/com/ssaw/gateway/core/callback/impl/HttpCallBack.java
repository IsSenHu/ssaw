package com.ssaw.gateway.core.callback.impl;

import com.ssaw.gateway.core.callback.CallBack;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

/**
 * @author HuSen
 * create on 2019/7/1 10:45
 */
@AllArgsConstructor
public class HttpCallBack implements CallBack<Object> {

    private ChannelHandlerContext connection;

    @Override
    public void execute(Object msg) {

    }
}
