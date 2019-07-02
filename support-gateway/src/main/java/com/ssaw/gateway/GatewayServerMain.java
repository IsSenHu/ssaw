package com.ssaw.gateway;

import com.ssaw.gateway.core.client.HttpClient;
import com.ssaw.gateway.server.GatewayServer;

/**
 * @author HuSen
 * create on 2019/6/28 17:33
 */
public class GatewayServerMain {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(10001);
        gatewayServer.start();
        HttpClient.getInstance().load();
        Runtime.getRuntime().addShutdownHook(new Thread(gatewayServer::close));
    }
}
