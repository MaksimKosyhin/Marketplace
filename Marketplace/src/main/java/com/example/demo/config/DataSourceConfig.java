package com.example.demo.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource(@Value("${spring.datasource.driverClassName}") String driverClassName, 
								 @Value("${spring.datasource.url}") String url,
								 @Value("${spring.datasource.username}") String username,
								 @Value("${spring.datasource.password}") String password) {
		return DataSourceBuilder
				.create()
				.driverClassName(driverClassName)
				.url(url)
				.username(username)
				.password(password)
				.build();
	}
	
	@Bean
	public Flyway flyway(DataSource dataSource) {
		Flyway flyway = Flyway
				.configure()
				.dataSource(dataSource)
				.load();
		
		flyway.migrate();
		
		return flyway;
	}
}
