package demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/3/27 11:58
 */
@Slf4j
public class DefaultMqPullConsumerDemo implements Runnable {

    private static final Map<MessageQueue, Long> OFFSET_TABLE = new HashMap<>();

    private String instanceName;

    public DefaultMqPullConsumerDemo(String instanceName) {
        this.instanceName = instanceName;
    }

    public static void main(String[] args) {
        DefaultMqPullConsumerDemo demo = new DefaultMqPullConsumerDemo("HeroPull");
        new Thread(demo).start();
    }

    @Override
    public void run() {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("defaultMQPullConsumerDemoGroup");
        consumer.setNamesrvAddr(DemoConstant.NAMESRV_ADDR);
        consumer.setInstanceName(instanceName);
        consumer.setConsumerTimeoutMillisWhenSuspend(100000);
        try {
            consumer.start();
            long l = System.currentTimeMillis();
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicTestTwo");
            for (MessageQueue mq : mqs) {
                long offset = consumer.fetchConsumeOffset(mq, true);
                log.info("Consume from the Queue:{}", mq);
                SINGLE_MQ:
                while (true) {
                    try {
                        PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                        log.info("pullResult:{}", pullResult);
                        pullResult.getMsgFoundList().forEach(x -> {
                            try {
                                log.info("Receive Message:{}", new String(x.getBody(), RemotingHelper.DEFAULT_CHARSET));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        });
                        putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                        switch (pullResult.getPullStatus()) {
                            case FOUND: break;
                            case NO_MATCHED_MSG: break;
                            case NO_NEW_MSG: break SINGLE_MQ;
                            case OFFSET_ILLEGAL: break;
                            default: break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long l1 = System.currentTimeMillis();
            log.info("消费花费时间:{}", l1 - l);
            consumer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSET_TABLE.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSET_TABLE.put(mq, offset);
    }
}