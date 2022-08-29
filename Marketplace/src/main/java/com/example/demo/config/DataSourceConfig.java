package com.example.demo.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceConfig {
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder
				.create()
				.driverClassName("org.postgresql.Driver")
				.url(env.getProperty("spring.datasource.url"))
				.username(env.getProperty("spring.datasource.username"))
				.password(env.getProperty("spring.datasource.password"))
				.build();
	}
	
	@Bean
	public Flyway flyway() {
		Flyway flyway = Flyway
				.configure()
				.dataSource(dataSource())
				.load();
		
		flyway.migrate();
		
		return flyway;
	}
}
