package com.bce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bce.repository.MongoTokenStore;
import com.bce.security.Authorities;
import com.bce.security.CustomAuthenticationEntryPoint;
import com.bce.security.CustomLogoutSuccessHandler;

/**
 * @author Narottam Singh
 * @Date 2018-05-30
 */
@Configuration
public class OAuth2Configuration {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Autowired
		private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

		@Autowired
		private CustomLogoutSuccessHandler customLogoutSuccessHandler;

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).and().logout()
					.logoutUrl("/oauth/logout").logoutSuccessHandler(customLogoutSuccessHandler).and().csrf()
					.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable().headers()
					.frameOptions().disable()
					.and().authorizeRequests().antMatchers("/hello/", "/login/").permitAll().antMatchers("/api/**")
					.authenticated();

		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
			implements EnvironmentAware {

		@Value("${authentication.oauth.clientid:clientid}")
		private String PROP_CLIENTID;

		@Value("${authentication.oauth.secret:secret}")
		private String PROP_SECRET;

		@Value("${authentication.oauth.tokenValidityInSeconds:60}")
		private Integer PROP_TOKEN_VALIDITY_SECONDS;

		@Value("${authentication.oauth.refreshTokenValidityInSeconds:120}")
		private Integer PROP_REFRESH_TOKEN_VALIDITY_SECONDS;

		@Bean
		public TokenStore tokenStore() {
			return new MongoTokenStore();
		}

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient(PROP_CLIENTID).scopes("read", "write")
					.authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name())
					.authorizedGrantTypes("password", "refresh_token").secret(PROP_SECRET)
					.accessTokenValiditySeconds(PROP_TOKEN_VALIDITY_SECONDS);
		}

		@Override
		public void setEnvironment(Environment environment) {
			
		}

	}

}
