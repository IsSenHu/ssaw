package com.ssaw.rocketmq.proxy;

import com.alibaba.fastjson.JSON;
import com.ssaw.rocketmq.annotation.MessageKey;
import com.ssaw.rocketmq.annotation.RocketMqMessage;
import com.ssaw.rocketmq.constant.CommonConstant;
import com.ssaw.rocketmq.util.RocketMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author HuSen
 * @date 2019/3/29 17:54
 */
@Slf4j
public class RocketMqProxy implements InvocationHandler {

    private Class<?> interfaceClass;

    Object bind(Class<?> cls) throws MQClientException, IllegalAccessException, InstantiationException {
        this.interfaceClass = cls;
        Class<?> messageClass = getMessageClass();
        Assert.notNull(messageClass, "not find message class");
        RocketMqMessage rocketMqMessage = messageClass.getDeclaredAnnotation(RocketMqMessage.class);
        Assert.notNull(rocketMqMessage, messageClass.getName() + "is not a RocketMqMessage");

        // 判断是否开始事务
        boolean transaction = rocketMqMessage.transaction();
        Class<? extends TransactionListener> transactionListener = rocketMqMessage.transactionListener();
        boolean openTransaction = transaction && transactionListener != TransactionListener.class;
        // 如果开启了事务
        if (openTransaction) {
            TransactionMQProducer producer = new TransactionMQProducer(rocketMqMessage.groupName());
            // 设置TransactionListener
            TransactionListener listener = transactionListener.newInstance();
            producer.setTransactionListener(listener);
            RocketMqUtil.setTransactionListener(messageClass.getName(), listener);
            setProducer(rocketMqMessage, producer);
        }
        // 如果没有开启事务
        else {
            DefaultMQProducer producer = new DefaultMQProducer(rocketMqMessage.groupName());
            setProducer(rocketMqMessage, producer);
        }
        // 设置sendCallBack
        Class<? extends SendCallback> sendCallBack = rocketMqMessage.sendCallBack();
        if (sendCallBack != SendCallback.class) {
            SendCallback callback = sendCallBack.newInstance();
            RocketMqUtil.setSendCallBack(messageClass.getName(), callback);
        }
        // 设置messageQueueSelector
        Class<? extends MessageQueueSelector> messageQueueSelector = rocketMqMessage.messageQueueSelector();
        if (messageQueueSelector != MessageQueueSelector.class) {
            MessageQueueSelector queueSelector = messageQueueSelector.newInstance();
            RocketMqUtil.setMessageQueueSelector(messageClass.getName(), queueSelector);
        }
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    private void setProducer(RocketMqMessage rocketMqMessage, DefaultMQProducer producer) throws MQClientException {
        producer.setNamesrvAddr(RocketMqUtil.getNamesrvAddr());
        producer.setInstanceName(rocketMqMessage.instanceName());
        producer.setDefaultTopicQueueNums(rocketMqMessage.queueNums());
        producer.setSendMsgTimeout(rocketMqMessage.sendTimeOut());
        producer.start();
        RocketMqUtil.setProducer(interfaceClass.getName(), producer);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Object arg = args[0];
            log.info("send message producer:{},{}", interfaceClass, arg);
            Class<?> aClass = arg.getClass();
            RocketMqMessage rocketMqMessage = aClass.getDeclaredAnnotation(RocketMqMessage.class);
            if (Objects.isNull(rocketMqMessage)) {
                return null;
            }
            Field[] declaredFields = aClass.getDeclaredFields();
            Field.setAccessible(declaredFields, true);
            String keyAsString = null;
            for (Field declaredField : declaredFields) {
                MessageKey messageKey = declaredField.getDeclaredAnnotation(MessageKey.class);
                if (Objects.nonNull(messageKey)) {
                    Object key = declaredField.get(arg);
                    keyAsString = key.toString();
                    log.info("message is:{}, key is:{}", rocketMqMessage, keyAsString);
                    break;
                }
            }
            DefaultMQProducer producer = RocketMqUtil.getProducer(interfaceClass.getName());
            SendCallback sendCallBack = RocketMqUtil.getSendCallBack(aClass.getName());
            MessageQueueSelector messageQueueSelector = RocketMqUtil.getMessageQueueSelector(aClass.getName());
            TransactionListener transactionListener = RocketMqUtil.getTransactionListener(aClass.getName());
            return realSendMessage(arg, rocketMqMessage, keyAsString, messageQueueSelector, sendCallBack, transactionListener, producer);
        } catch (Exception e) {
            log.error("send message fail:", e);
            return null;
        }
    }

    /**
     * 真正开始发送消息
     *
     * @param msg                  消息数据模型
     * @param message              消息注解
     * @param key                  业务键
     * @param messageQueueSelector 消息队列选择器
     * @param sendCallBack         异步回调函数
     * @param transactionListener  事务监听器
     * @return 发送结果 注意当启用异步发送消息时，返回Null
     */
    private SendResult realSendMessage(Object msg, RocketMqMessage message, Object key, MessageQueueSelector messageQueueSelector, SendCallback sendCallBack, TransactionListener transactionListener, DefaultMQProducer producer) {
        try {
            String body = toString(msg);
            if (log.isDebugEnabled()) {
                log.debug("start send message ---> :{}", body);
            }
            Message sendMessage = new Message(message.topicName(), message.tags(), key.toString(), body.getBytes(Charset.forName(RemotingHelper.DEFAULT_CHARSET)));
            sendMessage.putUserProperty(CommonConstant.TRANSACTION_LISTENER_TYPE, msg.getClass().getName());
            // 如果是事务消息则发送事务消息
            if (Objects.nonNull(transactionListener)) {
                return sendMessageInTransaction(sendMessage, producer, key);
            }
            if (Objects.isNull(messageQueueSelector) && Objects.isNull(sendCallBack)) {
                return producer.send(sendMessage, message.sendTimeOut());
            } else if (Objects.nonNull(messageQueueSelector) && Objects.isNull(sendCallBack)) {
                return producer.send(sendMessage, messageQueueSelector, key, message.sendTimeOut());
            } else if (Objects.isNull(messageQueueSelector)) {
                producer.send(sendMessage, sendCallBack, message.sendTimeOut());
                return null;
            } else {
                producer.send(sendMessage, messageQueueSelector, key, sendCallBack, message.sendTimeOut());
                return null;
            }
        } catch (Exception e) {
            log.error("send message fail : {}-fail:", msg, e);
        }
        return null;
    }

    /**
     * 发送事务消息
     *
     * @param message  发送的消息
     * @param producer 事务消息生产者
     * @param key      业务标识key
     * @return 事务消息发送结果
     * @throws MQClientException MQClientException
     */
    private TransactionSendResult sendMessageInTransaction(Message message, DefaultMQProducer producer, Object key) throws MQClientException {
        return producer.sendMessageInTransaction(message, key);
    }

    private Class<?> getMessageClass() {
        try {
            Type[] genericInterfaces = interfaceClass.getGenericInterfaces();
            Type[] actualTypeArguments = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();
            String typeName = actualTypeArguments[0].getTypeName();
            return Class.forName(typeName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 消息数据模型toString
     *
     * @param msg 消息数据模型
     * @return 消息数据模型的字符串形式
     */
    private String toString(Object msg) {
        return JSON.toJSONString(msg);
    }
}