package com.ssaw.rocketmq.abstracts;

import com.ssaw.rocketmq.interfaces.BaseConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author HuSen
 * @date 2019/3/28 13:40
 */
public abstract class AbstractBaseConsumer implements BaseConsumer {

    protected DefaultMQPushConsumer defaultMQPushConsumer;

    /**
     * 设置DefaultMQPushConsumer
     *
     * @param defaultMQPushConsumer DefaultMQPushConsumer
     */
    public abstract void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer);

    /**
     * 消息顺序监听
     *
     * @param messageExtList        消息列表
     * @param consumeOrderlyContext ConsumeOrderlyContext
     * @return 消息处理结果
     */
    public ConsumeOrderlyStatus messageOrderlyListening(List<MessageExt> messageExtList, ConsumeOrderlyContext consumeOrderlyContext) {
        return ConsumeOrderlyStatus.SUCCESS;
    }

    /**
     * 并发消息监听
     *
     * @param messageExtList             消息列表
     * @param consumeConcurrentlyContext ConsumeConcurrentlyContext
     * @return 消息处理结果
     */
    public ConsumeConcurrentlyStatus messageConcurrentListening(List<MessageExt> messageExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}