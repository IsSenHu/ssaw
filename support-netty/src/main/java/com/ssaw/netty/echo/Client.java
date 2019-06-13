package com.ssaw.netty.echo;

import com.ssaw.netty.echo.core.EchoClient;

/**
 * @author HuSen
 * @date 2019/6/5 17:06
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        new EchoClient("localhost", 12001).start();
    }
}