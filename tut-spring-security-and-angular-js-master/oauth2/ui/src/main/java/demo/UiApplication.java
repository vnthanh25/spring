package demo;

import java.io.Console;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@SpringBootApplication
@RestController
@EnableZuulProxy
@EnableOAuth2Sso
@EnableOAuth2Client
@EnableWebSecurity
public class UiApplication extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(UiApplication.class);

	@RequestMapping("/app")
	public AppInfo home() {
		return new AppInfo("App 1111");
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	  public String printWelcome(ModelMap model, Principal principal ) {

	      String name = principal.getName(); //get logged in username
	      model.addAttribute("username", name);
	      
	      return "hello";

	  }
	
	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
/*		new SpringApplicationBuilder(UiApplication.class)
        .properties("spring.config.name=client").run(args);
*/	}
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("password")
                .roles("USER");
    }
 */   
    
    @Autowired
    private OAuth2RestTemplate restTemplate;
/* 
    @Bean
    public OpenIdConnectFilter openIdConnectFilter() {
        OpenIdConnectFilter filter = new OpenIdConnectFilter("/login");
        //filter.setRestTemplate(restTemplate);
        return filter;
    }
*/ 

    @Bean
    OpenIdConnectFilter openIdConnectFilter() throws Exception {
    	OpenIdConnectFilter tokenProcessingFilter = new OpenIdConnectFilter("/login");
      tokenProcessingFilter.setAuthenticationManager(authenticationManagerBean());
      return tokenProcessingFilter;
    }    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .addFilterAfter(new OAuth2ClientContextFilter(), 
          AbstractPreAuthenticatedProcessingFilter.class)
        .addFilterAfter(openIdConnectFilter(), 
          OAuth2ClientContextFilter.class)
        .httpBasic()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()
//        .and().csrf().csrfTokenRepository(csrfTokenRepository())
//        .and().addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
//		.and()
//		.csrf().disable();
		.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }	
	
    
    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
			
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {

                        // Token is being added to the XSRF-TOKEN cookie.
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
				
			}
		};
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        //repository.setSessionAttributeName(("X-XSRF-TOKEN"));
        return repository;
    }
    
    
/*	
    @Autowired
    private OAuth2RestTemplate restTemplate;
 
    @Bean
    public OpenIdConnectFilter openIdConnectFilter() {
        OpenIdConnectFilter filter = new OpenIdConnectFilter("/login");
        //filter.setRestTemplate(restTemplate);
        return filter;
    }
*/	
/*	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.logout().and()
			.authorizeRequests()
				.antMatchers("/index.html", "/home.html", "/", "/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable();
//				.and()
//				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
			// @formatter:on
	}
*/
/*    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
*/    
/*    @Bean
    public OAuth2ProtectedResourceDetails googleOpenId() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId("acme");
        details.setClientSecret("acmesecret");
        details.setAccessTokenUri("http://localhost:9999/uaa/oauth/token");
        details.setUserAuthorizationUri("http://localhost:9999/uaa/oauth/authorize");
        details.setScope(Arrays.asList("openid", "email"));
        details.setPreEstablishedRedirectUri("http://localhost:1111/ui");
        details.setUseCurrentUri(false);
        return details;
    }
*/ 
/*    @Bean
    public OAuth2RestTemplate restTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(googleOpenId(), clientContext);
    }	
*/	

    
    
/*
	@Bean
	protected org.springframework.security.oauth2.client.OAuth2RestTemplate OAuth2RestTemplate(
			OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new org.springframework.security.oauth2.client.OAuth2RestTemplate(resource, context);
	}
*/	
	
	@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    } 	
/*	
    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }
*/	
    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }

/*    
    @Bean
    public MultipartResolver multipartResolver() {
       return new StandardServletMultipartResolver() {
         @Override
         public boolean isMultipart(HttpServletRequest request) {
            String method = request.getMethod().toLowerCase();
            if (!Arrays.asList("put", "post").contains(method)) {
               return false;
            }
            String contentType = request.getContentType();
            return (contentType != null &&contentType.toLowerCase().startsWith("multipart/"));
         }
       };
    }
*/    
}


class AppInfo {
	private String id = UUID.randomUUID().toString();
	private String content;

	AppInfo() {
	}

	public AppInfo(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
