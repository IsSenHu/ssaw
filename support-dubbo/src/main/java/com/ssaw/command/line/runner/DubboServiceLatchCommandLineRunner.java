package com.ssaw.command.line.runner;

import com.ssaw.shutdown.ShutdownLatchMBean;
import org.springframework.boot.CommandLineRunner;

/**
 * @author HuSen.
 * @date 2019/1/21 10:23.
 */
public class DubboServiceLatchCommandLineRunner implements CommandLineRunner {
    private ShutdownLatchMBean shutdownLatchMBean;

    public DubboServiceLatchCommandLineRunner(ShutdownLatchMBean shutdownLatchMBean) {
        this.shutdownLatchMBean = shutdownLatchMBean;
    }

    @Override
    public void run(String... args) throws Exception {
        shutdownLatchMBean.await();
    }

    public void setShutdownLatchMBean(ShutdownLatchMBean shutdownLatchMBean) {
        this.shutdownLatchMBean = shutdownLatchMBean;
    }
}
