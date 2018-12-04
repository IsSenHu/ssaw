package com.ssaw.support.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 认证中心客户端配置文件
 * @author HuSen.
 * @date 2018/11/28 17:53.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "statistical.certification.center")
public class StatisticalCertificationCenterClientProperties {
    private String id = "ResourceApp";
    private String secret = "$2a$10$4TO95izTMNCswJKqPB.6R.7.ADro.KPHhCw8CgxOBsgKG9ynRXyw.";
    private String redirectUrls = "http://baidu.com";
    private String authorizedGrantTypes = "authorization_code,refresh_token";
    private String scopes = "READ";
    private String resourceIds = "RESOURCE_ID";
    private Integer accessTokenValiditySeconds = 60 * 60 * 24;
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 30;
}
