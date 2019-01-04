package com.ssaw.commons.properties;

import feign.RequestInterceptor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author HuSen.
 * @date 2018/12/5 10:06.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "feign.header")
public class FeignHeaderProperties {

    private String bearerToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "bearer " + this.bearerToken);
    }
}
