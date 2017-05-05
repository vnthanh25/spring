package com.bim.server.filter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Created by kaspernissen on 04/04/2016.
 */
public class PreFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(PreFilter.class);
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
    	String tenantId = request.getHeader(TENANT_HEADER_NAME);
    	String apiUrl = "";
    	switch(tenantId) {
    		case "tenant_1":
    			apiUrl = "http://localhost:1111";
    			break;
    		case "tenant_2":
    			apiUrl = "http://localhost:2222";
    			break;
    	}
        try {
        	
			RequestContext.getCurrentContext().setRouteHost(new URL(apiUrl));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }
}
