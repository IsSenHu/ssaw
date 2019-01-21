package com.ssaw.inner.config;

import org.springframework.context.annotation.Import;

/**
 * @author HuSen.
 * @date 2019/1/8 19:10.
 */
@Import({WebSecurityConfig.class, Oauth2Config.class, ResourceServerConfig.class})
public class InnerSecurityConfig {
}
