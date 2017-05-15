package demo;

import java.security.KeyPair;
import java.security.Principal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Controller
//@SessionAttributes("authorizationRequest")
@EnableResourceServer
@EnableAuthorizationServer
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AuthserverApplication extends WebMvcConfigurerAdapter {

	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	@Configuration
	@Order(-20)
	protected static class LoginConfig extends WebSecurityConfigurerAdapter {

		
		@Autowired
		DataSource dataSource;

		@Autowired
		public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

		  auth.jdbcAuthentication().dataSource(dataSource)
		  .usersByUsernameQuery(
		  "select username, password, enabled from users where username=?")
		  .authoritiesByUsernameQuery(
		  "select username, role from user_roles where username=?");
		}
		
	    @Bean(name = "dataSource")
	    public DriverManagerDataSource dataSource() {
	        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
	        driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5432/authserver");
	        driverManagerDataSource.setUsername("postgres");
	        driverManagerDataSource.setPassword("admin");
	        return driverManagerDataSource;
	    }


	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	      http.authorizeRequests()
//	     .antMatchers("/hello").access("hasRole('ROLE_ADMIN')")  
	      .antMatchers("/", "/login**", "/webjars/**").permitAll()
	     .anyRequest().permitAll()
	     .and()
	       .formLogin().loginPage("/login")
	       .usernameParameter("username").passwordParameter("password")
	       .and()
			.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
	     .and()
	       .logout().logoutSuccessUrl("/login?logout") 
	      .and()
	      .exceptionHandling().accessDeniedPage("/403");
//	     .and()
//	       .csrf();
	    }
	    
	    
/*
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				.formLogin().loginPage("/login")
//				.permitAll()
				.usernameParameter("username").passwordParameter("password")
			.and()
				.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
			.and()
				.authorizeRequests().anyRequest().authenticated();
			// @formatter:on
		}
*/
/*		
		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.parentAuthenticationManager(authenticationManager);
		}
*/	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2AuthorizationConfig extends
			AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			KeyPair keyPair = new KeyStoreKeyFactory(
					new ClassPathResource("keystore.jks"), "foobar".toCharArray())
					.getKeyPair("test");
			converter.setKeyPair(keyPair);
			return converter;
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("acme")
					.secret("acmesecret")
					.authorizedGrantTypes("authorization_code", "refresh_token",
							"password").scopes("openid");
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			endpoints.authenticationManager(authenticationManager).accessTokenConverter(
					jwtAccessTokenConverter());
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer)
				throws Exception {
			oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
					"isAuthenticated()");
		}

	}
	
	
}
