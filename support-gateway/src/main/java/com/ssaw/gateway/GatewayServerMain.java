package com.ssaw.gateway;

import com.ssaw.gateway.core.entity.TargetServer;
import com.ssaw.gateway.handler.RequestToTargetHandler;
import com.ssaw.gateway.handler.RouteHandler;
import com.ssaw.gateway.server.GatewayServer;
import com.ssaw.gateway.server.ProxyServer;

/**
 * @author HuSen
 * create on 2019/6/28 17:33
 */
public class GatewayServerMain {
    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(10001);
        gatewayServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(gatewayServer::close));

        final String id = "husen";
        final String host = "127.0.0.1";
        final Integer port = 8080;
        ProxyServer proxyServer = new ProxyServer(id, host, port);
        TargetServer targetServer = new TargetServer();
        targetServer.setId(id);
        targetServer.setHost(host);
        targetServer.setPort(port);

        RequestToTargetHandler.addTarget(targetServer);
        RouteHandler.addProxy(proxyServer);

        proxyServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(proxyServer::close));
    }
}
