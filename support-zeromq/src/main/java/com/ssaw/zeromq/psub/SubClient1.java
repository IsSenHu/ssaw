package com.ssaw.zeromq.psub;

import lombok.extern.slf4j.Slf4j;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.StringTokenizer;

/**
 * @author HuSen
 * create on 2019/6/13 14:01
 */
@Slf4j
public class SubClient1 {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);
        subscriber.connect("tcp://localhost:5556");
        // 过滤
        String filter = (args.length > 0) ? args[0] : "10001";
        subscriber.subscribe(filter.getBytes());

        int updateNbr;
        long totalTemp = 0;
        int nbr = 100;
        for (updateNbr = 0; updateNbr < nbr; updateNbr++) {
            String recv = subscriber.recvStr(0).trim();
            StringTokenizer stringTokenizer = new StringTokenizer(recv, " ");
            Integer zipCode = Integer.valueOf(stringTokenizer.nextToken());
            Integer temperature = Integer.valueOf(stringTokenizer.nextToken());
            Integer relhumidity = Integer.valueOf(stringTokenizer.nextToken());
            totalTemp += temperature;
            log.info("{} {} {}", zipCode, temperature, relhumidity);
        }
        log.info("Average temperature for zipCode {} was {}", filter, totalTemp / updateNbr);
        log.info("...");
        subscriber.close();
        context.term();
    }
}
