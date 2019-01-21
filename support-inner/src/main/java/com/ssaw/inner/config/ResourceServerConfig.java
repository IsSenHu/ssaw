package com.ssaw.inner.config;

import com.ssaw.inner.properties.InnerSecurityProperties;
import com.ssaw.inner.properties.ResourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.Map;

/**
 * @author HuSen.
 * @date 2019/1/7 16:46.
 */
@SuppressWarnings("ALL")
@Slf4j
@Configuration
@EnableResourceServer
@EnableConfigurationProperties(InnerSecurityProperties.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String SPLIT_MARK = ",";
    private final InnerSecurityProperties innerSecurityProperties;

    @Autowired
    public ResourceServerConfig(InnerSecurityProperties innerSecurityProperties) {
        this.innerSecurityProperties = innerSecurityProperties;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(innerSecurityProperties.getResourceId());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers(innerSecurityProperties.getAntMatchers());
        Map<String, ResourceProperties> resourceConfig = innerSecurityProperties.getResourceConfig();
        if(MapUtils.isNotEmpty(resourceConfig)) {
            for (Map.Entry<String, ResourceProperties> resource : resourceConfig.entrySet()) {
                log.info("初始化资源拦截:{}", resource.getKey());
                ResourceProperties config = resource.getValue();
                http
                    .authorizeRequests()
                    .antMatchers(config.getAntMatchers().split(SPLIT_MARK)).access("#oauth2.hasScope('" + config.getScope() + "')");
            }
        }
    }
}
