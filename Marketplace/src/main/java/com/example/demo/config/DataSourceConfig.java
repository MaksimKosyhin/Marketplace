package com.example.demo.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DataSourceConfig {

	@Bean
	public Path imgDirectory() {
		return Paths.get("img");
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean DbProperties dbProperties() {
		DbProperties props = new DbProperties();
		props.setDriverClassName("org.postgresql.Driver");
		props.setUrl("jdbc:postgresql://localhost:5432/marketplace");
		props.setUsername("postgres");
		props.setPassword("pass");
		return props;
	}

	@Bean
	public DataSource dataSource(DbProperties props) {
		return DataSourceBuilder
				.create()
				.driverClassName(props.getDriverClassName())
				.url(props.getUrl())
				.username(props.getUsername())
				.password(props.getPassword())
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
