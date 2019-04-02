package demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author HuSen
 * @date 2019/3/27 14:32
 */
@Slf4j
public class TransactionDemo {

    public static void main(String[] args) {
        TransactionMqProducerDemo demo = new TransactionMqProducerDemo("Like");
        new Thread(demo).start();
    }

    private static class TransactionMqProducerDemo implements Runnable {

        private String instanceName;

        public TransactionMqProducerDemo(String instanceName) {
            this.instanceName = instanceName;
        }

        @Override
        public void run() {
            TransactionMQProducer producer = new TransactionMQProducer("TransactionMQProducerDemoGroup");
            producer.setNamesrvAddr(DemoConstant.NAMESRV_ADDR);
            producer.setInstanceName(instanceName);
            producer.setSendMsgTimeout(100000);
            producer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                    try {
                        String msg = new String(message.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        log.info("处理事务:{}, {}", msg, o.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            });

            try {
                producer.start();
                Message message = new Message("TestTopicFour", "TagA", "我转账给你10000元".getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.sendMessageInTransaction(message, UUID.randomUUID().toString());
                producer.shutdown();
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}