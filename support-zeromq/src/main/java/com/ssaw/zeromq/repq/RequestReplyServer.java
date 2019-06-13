package com.ssaw.zeromq.repq;


import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2019/6/13 13:26
 */
@Slf4j
public class RequestReplyServer {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket responder = context.socket(SocketType.REP);
        responder.bind("tcp://*:5555");
        while (!Thread.currentThread().isInterrupted()) {
            byte[] request = responder.recv(0);
            log.info("Received: {}", new String(request, StandardCharsets.UTF_8));
            TimeUnit.SECONDS.sleep(1);
            responder.send("World".getBytes(StandardCharsets.UTF_8), 0);
        }
        responder.close();
        context.term();
    }
}
