package com.marketplace.config;

import com.marketplace.app_analytics.AppAnalyticsRepository;
import com.marketplace.app_analytics.JdbcAppAnalyticsRepository;
import com.marketplace.category.CategoryRepository;
import com.marketplace.category.ColumnConverter;
import com.marketplace.category.JdbcCategoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public AppAnalyticsRepository appAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcAppAnalyticsRepository(jdbcTemplate);
    }
    @Bean
    public CategoryRepository categoryRepository(JdbcTemplate jdbcTemplate, ColumnConverter columnConverter) {
        return new JdbcCategoryRepository(jdbcTemplate, columnConverter);
    }
}
