package com.ssaw.gateway;

import com.ssaw.gateway.http.server.HttpServer;

/**
 * @author HuSen
 * create on 2019/6/28 14:03
 */
public class Server {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.start();
    }
}
