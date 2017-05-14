package demo;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenIdConnectFilter extends AbstractAuthenticationProcessingFilter {
	private static final Logger logger = LoggerFactory.getLogger(OpenIdConnectFilter.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	OAuth2RestTemplate restTemplate;
	
	public OpenIdConnectFilter(String defaultFilterProcessesUrl) {
	    super(defaultFilterProcessesUrl);
	    setAuthenticationManager(authenticationManager);
	    logger.info("33333333333333333333333333");
	}
	@Override
	public Authentication attemptAuthentication(
	  HttpServletRequest request, HttpServletResponse response) 
	  throws AuthenticationException, IOException, ServletException {
		logger.info("22222222222222222222222222222222");
	    OAuth2AccessToken accessToken;
	    try {
	        accessToken = restTemplate.getAccessToken();
	    } catch (OAuth2Exception e) {
	        throw new BadCredentialsException("Could not obtain access token", e);
	    }
	    try {
	        String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
	        Jwt tokenDecoded = JwtHelper.decode(idToken);
	        Map<String, String> authInfo = new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);
	
	        OpenIdConnectUserDetails user = new OpenIdConnectUserDetails(authInfo, accessToken);
	        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	    } catch (InvalidTokenException e) {
	        throw new BadCredentialsException("Could not obtain user details from token", e);
	    }
	}
}
