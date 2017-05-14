package demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class GoogleOpenIdConnectConfig {
/*    @Value("${google.clientId}")
    private String clientId;
 
    @Value("${google.clientSecret}")
    private String clientSecret;
 
    @Value("${google.accessTokenUri}")
    private String accessTokenUri;
 
    @Value("${google.userAuthorizationUri}")
    private String userAuthorizationUri;
 
    @Value("${google.redirectUri}")
    private String redirectUri;
*/ 
    @Bean
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
 
    @Bean
    public OAuth2RestTemplate googleOpenIdTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(googleOpenId(), clientContext);
    }
}