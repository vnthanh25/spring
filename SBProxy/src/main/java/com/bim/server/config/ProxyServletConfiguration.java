package com.bim.server.config;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class ProxyServletConfiguration {

	@Autowired
	private Environment environment;
	
	@Bean(name="server")
	public ServletRegistrationBean<ProxyServlet> servletRegistrationBean() {
		ServletRegistrationBean<ProxyServlet> servletRegistrationBean = new ServletRegistrationBean<ProxyServlet>(new ProxyServlet());
		servletRegistrationBean.addUrlMappings(environment.getProperty("proxy.server.servletUrl"));
		servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.server.targetUri"));
		
		return servletRegistrationBean;
	}

}
