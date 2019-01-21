package com.ssaw.inner.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2019/1/7 16:17.
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "inner.security")
public class InnerSecurityProperties implements Serializable {

    private String username = "admin";
    private String password = "admin";
    private String resourceId = "INNER_SYSTEM";
    private String antMatchers = "/api/**";
    private Map<String, ClientProperties> clientConfig = new HashMap<>(16);
    private Map<String, ResourceProperties> resourceConfig = new HashMap<>(16);

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
