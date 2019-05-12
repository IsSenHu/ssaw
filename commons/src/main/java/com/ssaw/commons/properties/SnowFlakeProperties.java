package com.ssaw.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @date 2019/4/18 17:31
 */
@Data
@ConfigurationProperties(prefix = "ssaw.snow-flake")
public class SnowFlakeProperties {

    /**
     * 数据中心ID
     * */
    private Long dataCenterId;

    /**
     * 机器ID
     * */
    private Long machineId;
}