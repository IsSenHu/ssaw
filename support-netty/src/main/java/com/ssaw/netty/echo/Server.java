package com.ssaw.netty.echo;

import com.ssaw.netty.echo.core.EchoServer;

/**
 * @author HuSen
 * @date 2019/6/5 17:06
 */
public class Server {
    public static void main(String[] args) {
        new EchoServer(12001, 12000).start();
    }
}