package com.ssaw.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author HuSen
 * create on 2019/6/13 17:41
 */
public class Demo {

    public static void main(String[] args) {
        String message = "Hello Disruptor";
        // 必须是2的N次方
        int ringBufferLength = 1024;
        Disruptor<MessageEvent> disruptor = new Disruptor<>(new MessageEventFactory(), ringBufferLength, new MessageThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new MessageEventHandler());
        disruptor.setDefaultExceptionHandler(new MessageExceptionHandler());
        RingBuffer<MessageEvent> ringBuffer = disruptor.start();
        MessageEventProducer producer = new MessageEventProducer(ringBuffer);
        producer.onData(message);
    }

    /**
     * 消息事件类
     */
    @Data
    private static class MessageEvent {
        /** 原始消息 */
        private String message;
    }

    /**
     * 消息事件工厂
     */
    private static class MessageEventFactory implements EventFactory<MessageEvent> {
        @Override
        public MessageEvent newInstance() {
            return new MessageEvent();
        }
    }

    /**
     * 消息转换类，负责将消息转换为事件
     */
    private static class MessageEventTranslator implements EventTranslatorOneArg<MessageEvent, String> {
        @Override
        public void translateTo(MessageEvent event, long sequence, String arg0) {
            event.setMessage(arg0);
        }
    }

    /**
     * 消费者线程工厂类
     */
    private static class MessageThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "Simple Disruptor Thread");
        }
    }

    /**
     * 消息事件处理类，这里只打印消息
     */
    private static class MessageEventHandler implements EventHandler<MessageEvent> {
        @Override
        public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) {
            System.out.println(event.getMessage());
        }
    }

    /**
     * 异常处理类
     */
    private static class MessageExceptionHandler implements ExceptionHandler<MessageEvent> {
        @Override
        public void handleEventException(Throwable ex, long sequence, MessageEvent event) {
            ex.printStackTrace();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            ex.printStackTrace();
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 消息生产者类
     */
    private static class MessageEventProducer {
        private RingBuffer<MessageEvent> ringBuffer;

        MessageEventProducer(RingBuffer<MessageEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        /**
         * 将接收的消息输出到RingBuffer
         *
         * @param message 消息
         */
        void onData(String message) {
            EventTranslatorOneArg<MessageEvent, String> translator = new MessageEventTranslator();
            ringBuffer.publishEvent(translator, message);
        }
    }
}
