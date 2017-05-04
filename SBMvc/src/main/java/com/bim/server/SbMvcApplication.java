package com.bim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SbMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbMvcApplication.class, args);
		/*
		new SpringApplicationBuilder(SbMvcApplication.class)
        .profiles("prod")
        .run(args);
        */
	}
}
