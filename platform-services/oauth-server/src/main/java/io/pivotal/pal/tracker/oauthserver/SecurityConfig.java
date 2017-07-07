package io.pivotal.pal.tracker.oauthserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class SecurityConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("tracker-client")
            .secret("supersecret")
            .authorizedGrantTypes("client_credentials")
            .scopes("openid");
    }

    @Override
    @SuppressWarnings("deprecation")
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
        throws Exception {
        oauthServer
            // NoOpPasswordEncoder is not secure but will be OK for local testing purposes.
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }
}
