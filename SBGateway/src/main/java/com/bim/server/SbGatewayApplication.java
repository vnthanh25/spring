package com.bim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.bim.server.filter.PreFilter;

@SpringBootApplication
@EnableZuulProxy
public class SbGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbGatewayApplication.class, args);
	}

    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }
}
