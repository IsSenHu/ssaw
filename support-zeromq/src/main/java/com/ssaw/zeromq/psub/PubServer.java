package com.ssaw.zeromq.psub;

import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Random;

/**
 * @author HuSen
 * create on 2019/6/13 13:55
 */
@Slf4j
public class PubServer {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(SocketType.PUB);
        publisher.bind("tcp://*:5556");

        Random random = new Random(System.currentTimeMillis());
        while (!Thread.currentThread().isInterrupted()) {
            int zipcode, temperature, relhumidity;
            zipcode = 10000 + random.nextInt(10000);
            temperature = random.nextInt(215) - 80 + 1;
            relhumidity = random.nextInt(50) + 10 + 1;
            String update = String.format("%05d %d %d", zipcode, temperature, relhumidity);
            log.info("update: {}", update);
            publisher.send(update, 0);
        }
        publisher.close();
        context.term();
    }
}
