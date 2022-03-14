package com.bce.oauth2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	private static final String RESOURCE_ID = "api";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID);
	}
	

	   @Autowired
       private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

       @Autowired
       private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	  
    @Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/h2/**").permitAll();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        
        
        httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .and()
        .logout()
        .logoutUrl("/oauth/logout")
        .logoutSuccessHandler(customLogoutSuccessHandler)
        .and().authorizeRequests()
		.antMatchers("/oauth/token", "/login/signup","/login/loginuser","/login/refreshtoken").permitAll().antMatchers("/api**")
		.authenticated().anyRequest().authenticated();
    }
    


}
