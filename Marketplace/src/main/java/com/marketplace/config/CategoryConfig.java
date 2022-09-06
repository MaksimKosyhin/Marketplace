package com.marketplace.config;

import com.marketplace.category.CategoryRepository;
import com.marketplace.category.JdbcCategoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class CategoryConfig {

    @Bean
    public CategoryRepository categoryRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCategoryRepository(jdbcTemplate);
    }
}