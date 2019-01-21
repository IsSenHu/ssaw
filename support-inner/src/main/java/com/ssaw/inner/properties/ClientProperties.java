package com.ssaw.inner.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author HuSen.
 * @date 2019/1/7 16:10.
 */
@Setter
@Getter
public class ClientProperties implements Serializable {

    private String clientId;
    private String secret;
    private String redirectUris;
    private String authorizedGrantTypes;
    private String scopes;
    private String resourceIds;
    private Integer accessTokenValiditySeconds = 60 * 60 * 24 * 30;
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 30 * 12;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
