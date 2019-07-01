package com.ssaw.gateway.core.callback;


/**
 * @author HuSen
 * create on 2019/7/1 10:42
 */
public interface CallBack<T> {

    /**
     * 回调
     *
     * @param msg 数据
     */
    void execute(T msg);
}
