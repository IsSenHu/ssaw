package com.ssaw.zeromq.repq;

import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2019/6/13 13:34
 */
@Slf4j
public class RequestReplyClient {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester = context.socket(SocketType.REQ);
        boolean connect = requester.connect("tcp://localhost:5555");
        log.info("连接成功: {}", connect);
        final int reqTimes = 100;
        for (int i = 0; i < reqTimes && connect; i++) {
            log.info("Sending Hello: {}", i);
            requester.send("Hello".getBytes(StandardCharsets.UTF_8), 0);
            byte[] bytes = requester.recv(0);
            log.info("Received: {}", new String(bytes, StandardCharsets.UTF_8));
        }
        requester.close();
        context.term();
    }
}
