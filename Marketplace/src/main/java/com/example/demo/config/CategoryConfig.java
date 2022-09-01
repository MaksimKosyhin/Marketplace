package com.example.demo.config;

import com.example.demo.category.CategoryRepository;
import com.example.demo.category.JdbcCategoryRepository;
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
