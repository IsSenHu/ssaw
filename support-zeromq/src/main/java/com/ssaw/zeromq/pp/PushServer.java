package com.ssaw.zeromq.pp;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2019/6/13 14:25
 */
public class PushServer {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket pusher = context.socket(SocketType.PUSH);
        pusher.bind("tcp://*:5557");
        final int max = 1000_000;
        for (int i = 1; i <= max; i++) {
            pusher.send(String.valueOf(i).getBytes(StandardCharsets.UTF_8));
        }
        pusher.close();
        context.term();
    }
}
