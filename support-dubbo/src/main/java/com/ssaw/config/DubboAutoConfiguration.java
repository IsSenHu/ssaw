package com.ssaw.config;

import com.ssaw.command.line.runner.DubboServiceLatchCommandLineRunner;
import com.ssaw.shutdown.ShutdownLatch;
import com.ssaw.shutdown.ShutdownLatchMBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author HuSen.
 * @date 2019/1/21 10:26.
 */
@Configuration
@Order
@Slf4j
@ConfigurationProperties(prefix = "shutdown.latch")
public class DubboAutoConfiguration {

    private String domainName = "com.ssaw.services.management";

    @Bean
    public ShutdownLatchMBean shutdownLatchMBean() {
        return new ShutdownLatch(domainName);
    }

    @Bean
    @ConditionalOnClass(com.alibaba.dubbo.rpc.Exporter.class)
    public DubboServiceLatchCommandLineRunner configureDubboServiceLatchCommandLineRunner() {
        log.info("DubboAutoConfiguration enabled by adding DubboServiceLatchCommandLineRunner.");
        return new DubboServiceLatchCommandLineRunner(shutdownLatchMBean());
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
