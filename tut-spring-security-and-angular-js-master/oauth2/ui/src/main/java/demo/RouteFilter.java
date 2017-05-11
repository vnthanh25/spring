package demo;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class RouteFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(RouteFilter.class);
	private static final String TENANT_HEADER_NAME = "X-TENANT-ID";

    @Override
    public String filterType() {
        return "route";
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
        //RequestContext ctx = RequestContext.getCurrentContext();
    	HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
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
