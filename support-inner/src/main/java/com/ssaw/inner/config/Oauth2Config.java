package com.ssaw.inner.config;

import com.ssaw.inner.properties.ClientProperties;
import com.ssaw.inner.properties.InnerSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.util.Map;

/**
 * @author HuSen.
 * @date 2019/1/7 16:31.
 */
@SuppressWarnings("ALL")
@Slf4j
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(InnerSecurityProperties.class)
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final InnerSecurityProperties innerSecurityProperties;
    @Autowired
    public Oauth2Config(AuthenticationManager authenticationManager, RedisConnectionFactory redisConnectionFactory, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, InnerSecurityProperties innerSecurityProperties) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.innerSecurityProperties = innerSecurityProperties;
    }

    private static final String KEY_PAIR = "myuserresource";
    private static final String MY_PASS = "mypass";
    private static final String KEY_STORE_PATH = "keystore.jks";
    private static final String SPLIT_MARK = ",";

    /**
     * @return 使用非对称加密算法来对Token进行签名
     */
    @Bean
    public JwtAccessTokenConverter getJwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 导入证书
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource(KEY_STORE_PATH), MY_PASS.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(KEY_PAIR));
        return converter;
    }

    /**
     * @return 使用Redis来存Token
     */
    @Bean
    public RedisTokenStore getRedisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * @param security 用来配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .realm(innerSecurityProperties.getResourceId())
                // 主要是让/oauth/token支持client_id以及client_secret作登录认证 要进行client校验就必须配置这个
                .allowFormAuthenticationForClients();
    }

    /**
     * @param endpoints 用来配置授权(authorization)以及令牌(token)的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                // 配置JwtAccessToken转换器
                .accessTokenConverter(getJwtAccessTokenConverter())
                // 配置TokenTore
                .tokenStore(getRedisTokenStore())
                // 配置UserDetailsServices
                .userDetailsService(userDetailsService)
                // 允许使用Get和Post方法访问端口
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
        Map<String, ClientProperties> clientConfig = innerSecurityProperties.getClientConfig();
        if(MapUtils.isNotEmpty(clientConfig)) {
            for (Map.Entry<String, ClientProperties> client : clientConfig.entrySet()) {
                log.info("内存ClientManager:{}", client.getKey());
                ClientProperties config = client.getValue();
                inMemory
                    .withClient(config.getClientId())
                    .secret(passwordEncoder.encode(config.getSecret()))
                    .redirectUris(config.getRedirectUris().split(SPLIT_MARK))
                    .authorizedGrantTypes(config.getAuthorizedGrantTypes().split(SPLIT_MARK))
                    .scopes(config.getScopes().split(SPLIT_MARK))
                    .resourceIds(config.getResourceIds().split(SPLIT_MARK))
                    .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
                    .refreshTokenValiditySeconds(config.getRefreshTokenValiditySeconds());
            }
        }
    }
}
