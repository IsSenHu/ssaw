package com.ssaw.rocketmq.annotation;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @date 2019/3/28 9:41
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketMqMessage {

    /**
     * 组名
     * */
    String groupName() default "";

    /**
     * 实例名称
     * */
    String instanceName() default "";

    /**
     * 主题名称
     */
    String topicName() default "";

    /**
     * 标签
     */
    String tags() default "";

    /**
     * 发送消息时发送队列选择器
     */
    Class<? extends MessageQueueSelector> messageQueueSelector() default MessageQueueSelector.class;

    /**
     * 异步发送消息回调函数
     */
    Class<? extends SendCallback> sendCallBack() default SendCallback.class;

    /**
     * 事务监听器 包括执行本地事务，进行事务回查
     */
    Class<? extends TransactionListener> transactionListener() default TransactionListener.class;

    /**
     * 是否开启事务
     */
    boolean transaction() default false;

    /**
     * 超时时间
     * */
    int sendTimeOut() default 3000;

    /**
     * 队列数
     * */
    int queueNums() default 8;
}
