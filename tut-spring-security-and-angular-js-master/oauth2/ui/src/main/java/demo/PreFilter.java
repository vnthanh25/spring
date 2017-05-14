/*package demo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.util.WebUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class PreFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(PreFilter.class);
	private static final String TENANT_HEADER_NAME = "X-TENANT-ID";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
    	return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
    	HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
    	HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
    	//String tenantId = request.getHeader(TENANT_HEADER_NAME);
    	String apiUrl = request.getRequestURI();
    	if(apiUrl.startsWith("/user/")) {
    		apiUrl = "http://localhost:9999/uaa/user";
    	} else if(apiUrl.startsWith("/ui/")){
    		apiUrl = "http://localhost:2222/ui";
    	} else if(apiUrl.startsWith("/resource/")){
    		apiUrl = "http://localhost:3333/resource";
    	} else if(apiUrl.startsWith("/bimserverwar/")){
    		apiUrl = "http://localhost:5555/bimserverwar";
            
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
                // Token is being added to the X-XSRF-TOKEN cookie.
                cookie = new Cookie("X-XSRF-TOKEN", token);
                cookie.setPath("/");
                response.addCookie(cookie);
                cookie = new Cookie("PHPSESSID", "4ub72qdam4rggrq9njjg1n80ou");
                cookie.setPath("/");
                response.addCookie(cookie);
                cookie = new Cookie("NG_TRANSLATE_LANG_KEY", "=%22en%22");
                cookie.setPath("/");
                response.addCookie(cookie);
                
                ctx.addZuulRequestHeader("X-XSRF-TOKEN", token);
                
                HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
                String remote_addr = request.getRemoteAddr();
                requestWrapper.setAttribute(arg0, arg1);.addHeader("remote_addr", remote_addr);
                
           }
            

			CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            // Spring Security will allow the Token to be included in this header name
            response.setHeader("X-CSRF-HEADER", token.getHeaderName());

            // Spring Security will allow the token to be included in this parameter name
            response.setHeader("X-CSRF-PARAM", token.getParameterName());

            // this is the value of the token to be included as either a header or an HTTP parameter
            response.setHeader("X-CSRF-TOKEN", token.getToken());

//            filterChain.doFilter(request, response);

    	} else if(apiUrl.startsWith("/redsun/")){
    		apiUrl = "http://localhost:5555/redsun";
    	} else if(apiUrl.startsWith("/redsun-service/")){
    		apiUrl = "http://localhost:5555/redsun-service";
    	} else if(apiUrl.startsWith("/springmvc/")){
    		apiUrl = "http://localhost:5555/springmvc";
    	} else {
    		apiUrl = "";
    	}
    	
    	if(apiUrl != "") {
	        try {
	        	
				RequestContext.getCurrentContext().setRouteHost(new URL(apiUrl));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }
}
*/