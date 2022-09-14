package com.marketplace.config;


import com.marketplace.repository.category.CategoryRepository;
import com.marketplace.repository.category.ColumnConverter;
import com.marketplace.repository.category.JdbcCategoryRepository;
import com.marketplace.repository.product.JdbcProductRepository;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.repository.user.JdbcUserRepository;
import com.marketplace.repository.user.UserRepository;
import com.marketplace.repository.app_analytics.AppAnalyticsRepository;
import com.marketplace.repository.app_analytics.JdbcAppAnalyticsRepository;
import com.marketplace.repository.order.JdbcOrderRepository;
import com.marketplace.repository.order.OrderRepository;

import com.marketplace.service.category.CategoryService;
import com.marketplace.service.category.CategoryServiceImpl;
import com.marketplace.service.product.ProductService;
import com.marketplace.service.product.ProductServiceImpl;
import com.marketplace.util.CategoryMapper;
import com.marketplace.util.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ProductService productService(ProductRepository repository, ProductMapper mapper) {
        return new ProductServiceImpl(repository, mapper);
    }

    @Bean
    public CategoryService categoryService(CategoryRepository repository, CategoryMapper mapper) {
        return new CategoryServiceImpl(repository, mapper);
    }

    @Bean
    public ProductRepository productRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcProductRepository(jdbcTemplate);
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }

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
