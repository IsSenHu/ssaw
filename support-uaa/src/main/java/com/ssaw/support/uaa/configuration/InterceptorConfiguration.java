package com.ssaw.support.uaa.configuration;

import com.ssaw.support.uaa.annotation.EnableUaa;
import com.ssaw.support.uaa.interceptor.RestTemplateUserContextInterceptor;
import com.ssaw.support.uaa.interceptor.UserContextInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author HuSen
 * @date 2019/4/27 15:16
 */
@Configuration
@ConditionalOnBean(annotation = EnableUaa.class)
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor());
    }

    /**
     * RestTemplate 拦截器，在发送请求前设置鉴权的用户上下文信息
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new RestTemplateUserContextInterceptor());
        return restTemplate;
    }
}