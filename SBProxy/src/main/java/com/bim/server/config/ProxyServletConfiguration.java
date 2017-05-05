package com.bim.server.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class ProxyServletConfiguration {

	@Autowired
	private Environment environment;

	@Bean(name="server1")
	public ServletRegistrationBean servletRegistrationBean1() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), environment.getProperty("proxy.server1.servletUrl"));
		servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.server1.targetUri"));
		servletRegistrationBean.setLoadOnStartup(1);
		
		return servletRegistrationBean;
	}

	@Bean(name="server2")
	public ServletRegistrationBean servletRegistrationBean2() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), environment.getProperty("proxy.server2.servletUrl"));
		servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.server2.targetUri"));
		servletRegistrationBean.setLoadOnStartup(2);
		
		return servletRegistrationBean;
	}
	
/*	
	@Bean(name="server1")
	public ServletRegistrationBean servletRegistrationBean1() {
		//ProxyServlet proxyServlet = new ProxyServlet();
        Map<String, String> initParams = new HashMap<String, String>();
	    initParams.put("targetUri", "http://localhost:1111");
	    initParams.put("targetUri", "http://localhost:2222");
	    
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet());
		//servletRegistrationBean.addUrlMappings(environment.getProperty("proxy.server1.servletUrl"), environment.getProperty("proxy.server2.servletUrl"));;
		//servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.server1.targetUri"));
		//servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.server2.targetUri"));
		servletRegistrationBean.setUrlMappings(new ArrayList<>(Arrays.asList("/server1/*", "/server2/*")));
		servletRegistrationBean.setInitParameters(initParams);
		
		return servletRegistrationBean;
	}
*/
}

/*
@Configuration
public class ProxyServletConfiguration {
	@Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return bf -> {
            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) bf;

            ProxyServlet proxyServlet1 = new ProxyServlet();
            Map<String, String> initParams1 = new HashMap<String, String>();
    	    initParams1.put("targetUri", "http://localhost:1111");
    	    BeanDefinitionBuilder beanDefinition1 = BeanDefinitionBuilder.genericBeanDefinition(ServletRegistrationBean.class)
            		.setScope(BeanDefinition.SCOPE_SINGLETON)
            		.addPropertyValue("servlet", proxyServlet1)
            		.addPropertyValue("urlMappings", new ArrayList<>(Arrays.asList("/server1/*")))
            		.addPropertyValue("initParameters", initParams1);
                    //.addConstructorArgReference("testBean1")
                    //.addConstructorArgValue(proxyServlet)
                    
    	    AbstractBeanDefinition bean1 = beanDefinition1.getBeanDefinition();
            beanFactory.registerBeanDefinition("server1", bean1);

            ProxyServlet proxyServlet = new ProxyServlet();
            Map<String, String> initParams = new HashMap<String, String>();
    	    initParams.put("targetUri", "http://localhost:2222");
    	    BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(ServletRegistrationBean.class)
            		.setScope(BeanDefinition.SCOPE_SINGLETON)
            		.addPropertyValue("servlet", proxyServlet)
            		.addPropertyValue("urlMappings", new ArrayList<>(Arrays.asList("/server2/*")))
            		.addPropertyValue("initParameters", initParams);
                    //.addConstructorArgReference("testBean1")
                    //.addConstructorArgValue(proxyServlet)
                    
    	    AbstractBeanDefinition bean = beanDefinition.getBeanDefinition();
            beanFactory.registerBeanDefinition("server2", bean);
        };
    }
}
*/
/*
@Configuration
public class ProxyServletConfiguration implements BeanFactoryPostProcessor {
	
	@Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)bf;
        ProxyServlet proxyServlet = new ProxyServlet();
        Map<String, String> initParams = new HashMap<String, String>();
	    initParams.put("targetUri", "http://localhost:2222");
	    BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(ServletRegistrationBean.class)
        		.setScope(BeanDefinition.SCOPE_SINGLETON)
        		.addPropertyValue("servlet", proxyServlet)
        		.addPropertyValue("urlMappings", new ArrayList<>(Arrays.asList("/server2/*")))
        		.addPropertyValue("initParameters", initParams);
                //.addConstructorArgReference("testBean1")
                //.addConstructorArgValue(proxyServlet)
                
	    
        beanFactory.registerBeanDefinition("testBean", beanDefinition.getBeanDefinition());
                
        
    }
}
*/
/*
@Configuration
public class ProxyServletConfiguration {
	
	  @Value("${proxy.elasticsearch.targetUri}")
	  private String targetUri;

	  @Value("${proxy.elasticsearch.servletUrl}")
	  private String servletUrl;


	  @Bean(name = "elasticsarchServlet")
	  public ServletRegistrationBean<ProxyServlet> elasticsearchServletRegistrationBean() {
	    ProxyServlet proxyServlet = new ProxyServlet();
	    
	    Map<String, String> initParams = new HashMap<String, String>();
	    initParams.put("targetUri", targetUri);

	    ServletRegistrationBean<ProxyServlet> servletRegistrationBean = new ServletRegistrationBean<ProxyServlet>(proxyServlet);
	    servletRegistrationBean.setName("elasticsearch");
	    servletRegistrationBean.setUrlMappings(new ArrayList<>(Arrays.asList(servletUrl)));
	    servletRegistrationBean.setInitParameters(initParams);

	    return servletRegistrationBean;
	  }
}
*/

/*
@Configuration
public class ProxyServletConfiguration implements EnvironmentAware {

	@Bean
	  public ServletRegistrationBean servletRegistrationBean(){
	    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), propertyResolver.getProperty("servlet_url"));
	    servletRegistrationBean.addInitParameter("targetUri", propertyResolver.getProperty("target_url"));
	    servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, propertyResolver.getProperty("logging_enabled", "false"));
	    return servletRegistrationBean;
	  }

	  private RelaxedPropertyResolver propertyResolver;

	  @Override
	  public void setEnvironment(Environment environment) {
	    this.propertyResolver = new RelaxedPropertyResolver(environment, "proxy.solr.");
	  }
	  
}
*/
/*
@Configuration
public class ProxyServletConfiguration {
	@Bean
	 public FilterRegistrationBean<DelegatingFilterProxy> securityFilterChainRegistration() {
	 DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
	 delegatingFilterProxy.setTargetBeanName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	 FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<DelegatingFilterProxy>(delegatingFilterProxy);
	 registrationBean.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	 registrationBean.addUrlPatterns("/*");
	 return registrationBean;
	 }
	
	 @Bean 
	 public ServletRegistrationBean<ServletContainer> jerseyServlet() {
	 ServletRegistrationBean<ServletContainer> registration = new ServletRegistrationBean<ServletContainer>(new ServletContainer(), "/rest/*");
	 // our rest resources will be available in the path /rest/*
	 registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyAutoConfiguration.class.getName());
	 return registration;
	 }
	 
	 @Bean
	 public ServletRegistrationBean<DispatcherServlet> dispatcherServlet() {
	 ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<DispatcherServlet>(new DispatcherServlet(), "/spring/*");

	 return registration;
	 }
}
*/