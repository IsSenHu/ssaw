package com.ssaw.rocketmq.config;

import com.ssaw.rocketmq.abstracts.AbstractBaseConsumer;
import com.ssaw.rocketmq.annotation.EnableRocketMq;
import com.ssaw.rocketmq.annotation.RocketMqConsumer;
import com.ssaw.rocketmq.properties.RocketMqBaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * @author HuSen
 * @date 2019/3/28 9:40
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketMqBaseProperties.class)
@ConditionalOnBean(annotation = EnableRocketMq.class)
public class RocketMqConfiguration {

    private final ApplicationContext applicationContext;

    private final RocketMqBaseProperties properties;

    @Autowired
    public RocketMqConfiguration(RocketMqBaseProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initConsumer() throws MQClientException, IllegalAccessException, InstantiationException {
        Map<String, Object> consumerBeans = applicationContext.getBeansWithAnnotation(RocketMqConsumer.class);
        for (Object bean : consumerBeans.values()) {
            log.info("find consumer:{}", bean.getClass().getName());
            RocketMqConsumer rocketMqConsumer = AnnotationUtils.findAnnotation(bean.getClass(), RocketMqConsumer.class);
            if (Objects.isNull(rocketMqConsumer)) {
                continue;
            }
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMqConsumer.groupName());
            consumer.setNamesrvAddr(properties.getNamesrvAddr());
            consumer.setInstanceName(rocketMqConsumer.instanceName());
            consumer.setConsumeFromWhere(rocketMqConsumer.consumeFromWhere());
            consumer.setMessageModel(rocketMqConsumer.messageModel());
            consumer.setConsumeTimeout(rocketMqConsumer.consumeTimeOut());
            consumer.setPullBatchSize(rocketMqConsumer.pullSize());
            consumer.setConsumeConcurrentlyMaxSpan(rocketMqConsumer.consumeConcurrentlyMaxSpan());
            consumer.setConsumeMessageBatchMaxSize(rocketMqConsumer.consumeMessageBatchMaxSize());
            consumer.subscribe(rocketMqConsumer.topicName(), rocketMqConsumer.tags());
            if (rocketMqConsumer.orderlyListenerAble()) {
                if (rocketMqConsumer.useImplMethod()) {
                    consumer.setMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
                        AbstractBaseConsumer abstractBaseConsumer = (AbstractBaseConsumer) bean;
                        return abstractBaseConsumer.messageOrderlyListening(list, consumeOrderlyContext);
                    });
                } else {
                    Class<? extends MessageListenerOrderly> messageListenerOrderly = rocketMqConsumer.messageListenerOrderly();
                    MessageListenerOrderly listenerOrderly = messageListenerOrderly.newInstance();
                    consumer.setMessageListener(listenerOrderly);
                }
            } else {
                if (rocketMqConsumer.useImplMethod()) {
                    consumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
                        AbstractBaseConsumer abstractBaseConsumer = (AbstractBaseConsumer) bean;
                        return abstractBaseConsumer.messageConcurrentListening(list, consumeConcurrentlyContext);
                    });
                } else {
                    Class<? extends MessageListenerConcurrently> messageListenerConcurrently = rocketMqConsumer.messageListenerConcurrently();
                    MessageListenerConcurrently listenerConcurrently = messageListenerConcurrently.newInstance();
                    consumer.setMessageListener(listenerConcurrently);
                }
            }
            consumer.start();
        }
    }
}