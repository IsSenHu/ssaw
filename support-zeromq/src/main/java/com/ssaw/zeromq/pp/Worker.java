package com.ssaw.zeromq.pp;

import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2019/6/13 14:31
 */
@Slf4j
public class Worker extends Thread {

    @Override
    public void run() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket puller = context.socket(SocketType.PULL);
        ZMQ.Socket pusher = context.socket(SocketType.PUSH);
        pusher.connect("tcp://localhost:5558");
        puller.connect("tcp://localhost:5557");
        while (!Thread.currentThread().isInterrupted()) {
            String message = new String(puller.recv(), StandardCharsets.UTF_8);
            Integer value = Integer.valueOf(message);
            log.info("Received: {}", value);
            pusher.send(message.getBytes(StandardCharsets.UTF_8));
        }
        puller.close();
        pusher.close();
        context.term();
    }

    public static void main(String[] args) {
        int threads = 5;
        for (int i = 0; i < threads; i++) {
            new Worker().start();
        }
    }
}
