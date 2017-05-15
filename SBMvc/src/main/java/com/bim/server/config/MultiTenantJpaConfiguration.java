package com.bim.server.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bim.server.config.MultiTenantProperties.DataSourceProperties;

@Configuration
@EnableConfigurationProperties({ MultiTenantProperties.class, JpaProperties.class })
@ImportResource(locations = { "classpath:applicationContent.xml" })
@EnableTransactionManagement
public class MultiTenantJpaConfiguration {

	@Autowired
	private org.springframework.core.env.Environment environment;

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	@Bean(name = "dataSources" )
	@Primary
	public Map<String, DataSource> dataSources() {
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceProperties dsProperties : this.multiTenantProperties.getDataSources()) {
			DataSourceBuilder factory = DataSourceBuilder
				.create()
				.url(dsProperties.getUrl())
				.username(dsProperties.getUsername())
				.password(dsProperties.getPassword())
				.driverClassName(dsProperties.getDriverClassName());
			result.put(dsProperties.getTenantId(), factory.build());
		}
		return result;
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		// Autowires dataSources
		return new DataSourceMultiTenantConnectionProviderImpl();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new TenantIdentifierResolverImpl();
	}
	
	/*
	 * Provider specific adapter.
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
		CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

		// No dataSource is set to resulting entityManagerFactoryBean
		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setPackagesToScan(new String[] { environment.getRequiredProperty("spring.model-scan") });
		result.setJpaVendorAdapter(jpaVendorAdapter());
		result.setJpaPropertyMap(hibernateProps);

		return result;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	/* 
	 * Transaction manager setup.
	 */
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}
}