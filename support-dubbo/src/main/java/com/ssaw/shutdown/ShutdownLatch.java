package com.ssaw.shutdown;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author HuSen.
 * @date 2019/1/18 17:37.
 */
public class ShutdownLatch implements ShutdownLatchMBean {
    private AtomicBoolean running = new AtomicBoolean(false);
    private static final long CHECK_INTERVAL_IN_SECONDS = 10;
    private String domain = "com.ssaw.lifecycles";

    public ShutdownLatch() {}

    public ShutdownLatch(String domain) {
        this.domain = domain;
    }

    @Override
    public void await() throws Exception {
        if(running.compareAndSet(false, true)) {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(this, new ObjectName(domain, "name", "ShutdownLatch"));
            while (running.get()) {
                TimeUnit.SECONDS.sleep(CHECK_INTERVAL_IN_SECONDS);
            }
        }
    }

    @Override
    public String shutdown() {
        if(running.compareAndSet(true, false)) {
            return "shutdown signal sent, shutting down..";
        } else {
            return "shutdown signal had been sent, no need again and again and again..";
        }
    }
}
