package com.marketplace.config;


import com.marketplace.category.CategoryRepository;
import com.marketplace.category.ColumnConverter;
import com.marketplace.category.JdbcCategoryRepository;
import com.marketplace.product.JdbcProductRepository;
import com.marketplace.product.ProductRepository;
import com.marketplace.user.JdbcUserRepository;
import com.marketplace.user.UserRepository;
import com.marketplace.app_analytics.AppAnalyticsRepository;
import com.marketplace.app_analytics.JdbcAppAnalyticsRepository;
import com.marketplace.order.JdbcOrderRepository;
import com.marketplace.order.OrderRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ProductRepository productRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcProductRepository(jdbcTemplate);

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);

    @Bean
    public AppAnalyticsRepository appAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcAppAnalyticsRepository(jdbcTemplate);
    }
    
    @Bean
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcOrderRepository(jdbcTemplate);
    }

    @Bean
    public CategoryRepository categoryRepository(JdbcTemplate jdbcTemplate, ColumnConverter columnConverter) {
        return new JdbcCategoryRepository(jdbcTemplate, columnConverter);
    }
}
