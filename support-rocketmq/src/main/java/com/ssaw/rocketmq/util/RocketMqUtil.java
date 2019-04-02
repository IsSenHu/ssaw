package com.ssaw.rocketmq.util;

import com.ssaw.rocketmq.properties.RocketMqBaseProperties;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @date 2019/4/2 15:23
 */
public class RocketMqUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        RocketMqUtil.applicationContext = applicationContext;
    }

    private static final Map<String, DefaultMQProducer> ROCKET_MQ_PRODUCER_MAP = new HashMap<>();
    private static final Map<String, MessageQueueSelector> MESSAGE_QUEUE_SELECTOR_MAP = new HashMap<>();
    private static final Map<String, SendCallback> SEND_CALLBACK_MAP = new HashMap<>();
    private static final Map<String, TransactionListener> TRANSACTION_LISTENER_MAP = new HashMap<>();

    public static DefaultMQProducer getProducer(String producerClassName) {
        return ROCKET_MQ_PRODUCER_MAP.get(producerClassName);
    }

    public static void setProducer(String producerClassName, DefaultMQProducer producer) {
        ROCKET_MQ_PRODUCER_MAP.put(producerClassName, producer);
    }

    public static MessageQueueSelector getMessageQueueSelector(String messageClassName) {
        return MESSAGE_QUEUE_SELECTOR_MAP.get(messageClassName);
    }

    public static void setMessageQueueSelector(String messageClassName, MessageQueueSelector messageQueueSelector) {
        MESSAGE_QUEUE_SELECTOR_MAP.put(messageClassName, messageQueueSelector);
    }

    public static SendCallback getSendCallBack(String messageClassName) {
        return SEND_CALLBACK_MAP.get(messageClassName);
    }

    public static void setSendCallBack(String messageClassName, SendCallback sendCallback) {
        SEND_CALLBACK_MAP.put(messageClassName, sendCallback);
    }

    public static TransactionListener getTransactionListener(String messageClassName) {
        return TRANSACTION_LISTENER_MAP.get(messageClassName);
    }

    public static void setTransactionListener(String messageClassName, TransactionListener transactionListener) {
        TRANSACTION_LISTENER_MAP.put(messageClassName, transactionListener);
    }

    public static String getNamesrvAddr() {
        RocketMqBaseProperties properties = applicationContext.getBean(RocketMqBaseProperties.class);
        return properties.getNamesrvAddr();
    }
}