package com.ssaw.rocketmq.impl;

import com.ssaw.rocketmq.abstracts.AbstractBaseConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * @author HuSen
 * @date 2019/3/28 13:43
 */
public class BaseConsumerImpl extends AbstractBaseConsumer {

    @Override
    public void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
        this.defaultMQPushConsumer = defaultMQPushConsumer;
    }
}