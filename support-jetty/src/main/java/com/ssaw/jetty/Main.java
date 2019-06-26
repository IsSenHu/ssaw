package com.ssaw.jetty;

import com.ssaw.jetty.server.HttpServer;
import com.ssaw.jetty.servlet.impl.TestServlet;

import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 12:02
 */
public class Main {
    public static void main(String[] args) {
        final HttpServer hetty = new HttpServer("hetty", new InetSocketAddress(8080));
        hetty.addServlet("say", new TestServlet());
        hetty.start();
        Runtime.getRuntime().addShutdownHook(new Thread(hetty::shutdown));
    }
}
