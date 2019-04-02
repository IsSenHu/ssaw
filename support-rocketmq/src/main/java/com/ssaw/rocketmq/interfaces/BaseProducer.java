package com.ssaw.rocketmq.interfaces;

import org.apache.rocketmq.client.producer.SendResult;

/**
 * 基础生产者模版
 *
 * @author HuSen
 * @date 2019/3/28 9:48
 */
public interface BaseProducer<T> {

    /**
     * 发送消息
     *
     * @param msg 发送的消息
     * @return 发送结果
     */
    default SendResult send(T msg) {
        System.out.println(msg);
        return new SendResult();
    }
}
