package com.rpicloud.filters.pre;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.util.HTTPRequestUtils;

public class ExampleSurgicalDebugFilterBean extends ZuulFilter {
 
    @Override
    public String filterType() {
        return "pre";
    }
 
    @Override
    public int filterOrder() {
        return 96;
    }
 
    @Override
	public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRequest().getRequestURI().matches("/server2/**");
    }
 
    @Override
    public Object run() {
 
        try {
			RequestContext.getCurrentContext().setRouteHost(new URL("http://localhost:2222"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (HTTPRequestUtils.getInstance().getQueryParams() == null) {
            RequestContext.getCurrentContext().setRequestQueryParams(new HashMap<String, List<String>>());
        }
        List<String> requested = new ArrayList<String>();
        requested.add("true");
        HTTPRequestUtils.getInstance().getQueryParams().put("debugRequest", requested);
        RequestContext.getCurrentContext().setDebugRequest(true);
        RequestContext.getCurrentContext().setSendZuulResponse(true);
 
        return null;
    }
 
}
 
