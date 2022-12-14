package com.marketplace.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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

	@Bean
	public Flyway flyway(DataSource dataSource) {
		Flyway flyway = Flyway
				.configure()
				.dataSource(dataSource)
				.load();

		flyway.migrate();

		return flyway;
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
	public DbProperties dbProperties() {
		return new DbProperties();
	}
}
