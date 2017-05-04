package com.bim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/*
@SpringBootConfiguration
@ContextConfiguration(classes = SbMvcApplication.class)
@Configuration
@ComponentScan
@EnableJpaRepositories
@Import(RepositoryRestConfiguration.class)
@EnableAutoConfiguration
@PropertySource("application.yml")
*/
/*
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.bim.server.repository"})
@ComponentScan(basePackages = {"com.bim.server.controller","com.bim.server.model"})
@EnableWebMvc
@PropertySources(value = {@PropertySource("classpath:application.yml")})
@EntityScan(basePackages={"com.bim.server.model"})
*/
/*
@Import(JpaConfig.class)
@SpringBootApplication(scanBasePackages={"com.bim.server"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
*/
//@SpringBootApplication
@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class }
	)
@ComponentScan({"com.bim.server"})
@EntityScan("com.bim.server.model")
@EnableJpaRepositories("com.bim.server.repository")
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
