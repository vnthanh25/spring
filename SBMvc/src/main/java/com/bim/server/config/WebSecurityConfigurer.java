package com.bim.server.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@Order(-1)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	private CsrfMatcher csrfRequestMatcher = new CsrfMatcher();
	
   @Override
   protected void configure(HttpSecurity http) throws Exception {
	   http
		.csrf().disable()
//	    .csrf().requireCsrfProtectionMatcher(csrfRequestMatcher)
//	    .and()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "/*").permitAll();
   }
}


class CsrfMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {

    	return request.getRequestURL().indexOf("/server/api/files/upload") > -1;
    }
}