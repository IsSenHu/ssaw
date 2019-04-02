package com.ssaw.rocketmq.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @date 2019/3/28 9:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ssaw.rocketmq")
public class RocketMqBaseProperties {
    /** namesrv server 地址 */
    private String namesrvAddr;
}