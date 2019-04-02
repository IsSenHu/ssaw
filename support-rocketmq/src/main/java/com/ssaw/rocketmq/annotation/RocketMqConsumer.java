package com.ssaw.rocketmq.annotation;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/3/28 9:41
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RocketMqConsumer {

    /** 消费组名称 */
    String groupName() default "";

    /** 实例名称 */
    String instanceName() default "";

    /** 主题名称 */
    String topicName() default "";

    /** 过滤的标签 */
    String tags() default "*";

    /** 消费模式 默认集群 */
    MessageModel messageModel() default MessageModel.CLUSTERING;

    /** 从哪里开始消费 */
    ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;

    /** 默认超时消费时间 */
    int consumeTimeOut() default 3000;

    /** 一次最大拉取的批量大小 */
    int pullSize() default 32;

    /** 批量消费的最大消息条数 */
    int consumeMessageBatchMaxSize() default 10;

    /** 消费者并发数量 */
    int consumeConcurrentlyMaxSpan() default 8;

    /** 顺序 消息监听器 */
    Class<? extends MessageListenerOrderly> messageListenerOrderly() default MessageListenerOrderly.class;

    /** 并发消息监听器 */
    Class<? extends MessageListenerConcurrently> messageListenerConcurrently() default MessageListenerConcurrently.class;

    /** 是否使用顺序监听器 */
    boolean orderlyListenerAble() default false;

    /** 是否在BaseConsumer实现类里面实现消息处理逻辑 */
    boolean useImplMethod() default false;

    /** 最大消费次数 */
    int maxConsumeTimes() default 3;
}
