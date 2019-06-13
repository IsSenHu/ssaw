package com.ssaw.netty.echo;

import com.ssaw.netty.echo.core.EchoServer2;

/**
 * @author HuSen
 * @date 2019/6/5 17:06
 */
public class Server2 {
    public static void main(String[] args) throws InterruptedException {
        new EchoServer2(12000).start();
    }
}