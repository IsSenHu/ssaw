package com.ssaw.jetty.server;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 11:56
 */
@Slf4j
public class HttpServer {
    private Server server;
    private InetSocketAddress address;
    protected ServletContextHandler servletHandler;

    public HttpServer(String serverName, InetSocketAddress address) {
        this.address = address;
        this.servletHandler = new ServletContextHandler(1);
        this.servletHandler.setContextPath("/" + serverName);
    }

    public void addServlet(String name, HttpServlet servlet) {
        this.servletHandler.addServlet(new ServletHolder(servlet), "/" + name);
    }

    public void start() {
        try {
            this.server = new Server(this.address);
            this.server.setStopAtShutdown(true);
            this.server.setHandler(this.servletHandler);
            log.info("server start.");
            this.server.start();
        } catch (Exception var2) {
            log.error("", var2);
        }
    }

    public void shutdown() {
        try {
            if (this.server != null) {
                this.server.stop();
            }
        } catch (Exception var2) {
            log.error("", var2);
        }

    }
}
