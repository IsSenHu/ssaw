package com.ssaw.gateway.core.callback;

import io.netty.channel.Channel;

/**
 * @author HuSen
 * create on 2019/7/1 10:42
 */
public interface CallBack<T> {

    /**
     * 回调
     *
     * @param channel 连接
     * @param msg     数据
     */
    void execute(Channel channel, T msg);
}
