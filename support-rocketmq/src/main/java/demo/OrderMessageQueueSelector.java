package demo;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author HuSen
 * @date 2019/3/27 13:55
 */
public class OrderMessageQueueSelector implements MessageQueueSelector {

    @Override
    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
        int id = Integer.parseInt(o.toString());
        int idMainIndex = id / 10;
        int size = list.size();
        int index = idMainIndex % size;
        MessageQueue messageQueue = list.get(index);
        System.out.println(index + ":" + messageQueue.getQueueId());
        return messageQueue;
    }
}