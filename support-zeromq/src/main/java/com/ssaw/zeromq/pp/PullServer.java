package com.ssaw.zeromq.pp;

import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2019/6/13 14:46
 */
@Slf4j
public class PullServer {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket puller = context.socket(SocketType.PULL);
        puller.bind("tcp://*:5558");
        Integer value = 0;
        final int max = 1000_000;
        long result = 0;
        while (value < max) {
            String message = new String(puller.recv(), StandardCharsets.UTF_8);
            value = Integer.valueOf(message);
            log.info("collect: {}", value);
            result += value;
        }
        log.info("total: {}, avg: {}", result, result / max);
    }
}
