package demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author HuSen
 * @date 2019/3/27 10:58
 */
@Slf4j
public class DefaultMqProducerDemo implements Runnable {

    public static void main(String[] args) {
        new Thread(new DefaultMqProducerDemo()).start();
    }

    @Override
    public void run() {
        DefaultMQProducer producer = new DefaultMQProducer("defaultMQProducerDemo");
        producer.setNamesrvAddr(DemoConstant.NAMESRV_ADDR);
        producer.setDefaultTopicQueueNums(8);
        try {
            producer.start();
            long l = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                Message message = new Message("TopicTestSix", "", "", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                message.putUserProperty("a", String.valueOf(i));
                message.putUserProperty("b", "hello");
                SendResult sendResult = producer.send(message, new OrderMessageQueueSelector(), i, 100000);
                log.info("发送结果:{}", sendResult);
            }
            long l1 = System.currentTimeMillis();
            log.info("花费时间:{}", l1 - l);
            producer.shutdown();
        } catch (MQClientException | UnsupportedEncodingException | InterruptedException | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }
    }
}