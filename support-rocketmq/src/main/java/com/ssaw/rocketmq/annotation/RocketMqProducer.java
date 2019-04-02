package com.ssaw.rocketmq.annotation;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;
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
public @interface RocketMqProducer {

    /** 生产组名称 */
    String groupName() default "";

    /** 实例名称 */
    String instanceName() default "";

    /** 发送消息超时时间 */
    int sendTimeOut() default 3000;

    /** 每个broker有多少个队列 */
    int defaultQueueNums() default 8;

    /** 主题名称 */
    String topicName() default "";

    /** 发送标签 */
    String tags() default "";

    /** 消息的key */
    String key() default "";

    /** 同步发送 */
    boolean sync() default true;

    /** 是否开启事务 */
    boolean transaction() default false;

    /** 开启事务时是否还需要DefaultMQProducer */
    boolean transactionOpenNeedDefault() default false;

    /** 异步发送需要指定CallBack */
    Class<? extends SendCallback> callBack() default SendCallback.class;

    /** 开启事务需要指定 */
    Class<? extends TransactionListener> transactionListener() default TransactionListener.class;

    /** 队列选择器 */
    Class<? extends MessageQueueSelector> queueSelector() default MessageQueueSelector.class;

    /** 是否使用队列选择器 */
    boolean queueSelectorAble() default false;

    /** 是否完全顺序发送 */
    boolean absoluteOrderly() default false;
}
