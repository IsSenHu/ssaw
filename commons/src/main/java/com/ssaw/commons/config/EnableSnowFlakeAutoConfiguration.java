package com.ssaw.commons.config;

import com.ssaw.commons.id.DefaultIdService;
import com.ssaw.commons.id.SnowFlake;
import com.ssaw.commons.properties.SnowFlakeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * @date 2019/4/18 17:36
 */
@Configuration
@EnableConfigurationProperties(SnowFlakeProperties.class)
public class EnableSnowFlakeAutoConfiguration {

    private final SnowFlakeProperties snowFlakeProperties;

    @Autowired
    public EnableSnowFlakeAutoConfiguration(SnowFlakeProperties snowFlakeProperties) {
        this.snowFlakeProperties = snowFlakeProperties;
    }

    @Bean
    public DefaultIdService defaultIdService() {
        Long dataCenterId = snowFlakeProperties.getDataCenterId();
        Long machineId = snowFlakeProperties.getMachineId();
        Assert.notNull(dataCenterId, "数据中心ID必须提供");
        Assert.notNull(machineId, "机器ID必须提供");

        SnowFlake snowFlake = new SnowFlake(dataCenterId, machineId);
        return new DefaultIdService(snowFlake);
    }
}