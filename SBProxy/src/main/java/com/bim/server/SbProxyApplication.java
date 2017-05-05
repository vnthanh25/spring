package com.bim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbProxyApplication {

	public static void main(String[] args) {
		//AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProxyServletConfiguration.class);

		SpringApplication.run(SbProxyApplication.class, args);
	}
}
