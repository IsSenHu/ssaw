package demo;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author HuSen
 * @date 2019/3/27 10:37
 */
@Slf4j
public class DefaultMqPushConsumerDemo implements Runnable {

    private String instanceName;

    private DefaultMqPushConsumerDemo(String instanceName) {
        this.instanceName = instanceName;
    }

    public static void main(String[] args) {
        DefaultMqPushConsumerDemo demo = new DefaultMqPushConsumerDemo("defaultMQPushConsumerDemoInstanceOne");
        new Thread(demo).start();
//        DefaultMqPushConsumerDemo demo2 = new DefaultMqPushConsumerDemo("defaultMQPushConsumerDemoInstanceTwo");
//        new Thread(demo2).start();
    }

    @Override
    public void run() {
        // 使用DefaultMQPushConsumer主要是设置好各种参数和传入处理消息的函数。
        // 系统收到消息后自动调用处理函数来处理消息，自动保存Offset，而且加入新的DefaultMQPushConsumer后会自动做负载均衡
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("defaultMQPushConsumerDemoGroup");
        consumer.setNamesrvAddr(DemoConstant.NAMESRV_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setInstanceName(instanceName);
        try {
            consumer.subscribe("ssaw-ui-log-request", "*");
            consumer.registerMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
                list.forEach(x -> {
                    try {
                        log.info("{}-{}-Receive Message:{}", instanceName, x.getQueueId(), new String(x.getBody(), RemotingHelper.DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
                return ConsumeOrderlyStatus.SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}