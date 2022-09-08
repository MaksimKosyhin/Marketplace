package com.marketplace.config;

import com.marketplace.category.CategoryRepository;
import com.marketplace.category.ColumnConverter;
import com.marketplace.category.JdbcCategoryRepository;
import com.marketplace.user.JdbcUserRepository;
import com.marketplace.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }

    @Bean
    public CategoryRepository categoryRepository(JdbcTemplate jdbcTemplate, ColumnConverter columnConverter) {
        return new JdbcCategoryRepository(jdbcTemplate, columnConverter);
    }
}
