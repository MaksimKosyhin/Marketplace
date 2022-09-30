package com.marketplace.config;


import com.marketplace.repository.app_analytics.AppAnalyticsRepository;
import com.marketplace.repository.app_analytics.JdbcAppAnalyticsRepository;
import com.marketplace.repository.category.CategoryRepository;
import com.marketplace.repository.category.ColumnConverter;
import com.marketplace.repository.category.JdbcCategoryRepository;
import com.marketplace.repository.order.JdbcOrderRepository;
import com.marketplace.repository.order.OrderRepository;
import com.marketplace.repository.product.JdbcProductRepository;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.repository.shop.JdbcShopRepository;
import com.marketplace.repository.shop.ShopRepository;
import com.marketplace.service.app_analytics.AppAnalyticsService;
import com.marketplace.service.app_analytics.AppAnalyticsServiceImpl;
import com.marketplace.service.category.CategoryService;
import com.marketplace.service.category.CategoryServiceImpl;
import com.marketplace.service.order.OrderService;
import com.marketplace.service.order.OrderServiceImpl;
import com.marketplace.service.product.ProductService;
import com.marketplace.service.product.ProductServiceImpl;
import com.marketplace.service.shop.ShopService;
import com.marketplace.service.shop.ShopServiceImpl;
import com.marketplace.util.CategoryMapper;
import com.marketplace.util.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ShopService shopService(ShopRepository repository, ImageLoader loader) {
        return new ShopServiceImpl(repository, loader);
    }

    @Bean
    public AppAnalyticsService appAnalyticsService(AppAnalyticsRepository repository) {
        return new AppAnalyticsServiceImpl(repository);
    }

    @Bean
    public OrderService orderService(OrderRepository repository) {
        return new OrderServiceImpl(repository);
    }

    @Bean
    public ProductService productService(
            ProductRepository repository,
            ProductMapper mapper,
            ImageLoader imageLoader) {

        return new ProductServiceImpl(repository, mapper, imageLoader);
    }

    @Bean
    public CategoryService categoryService(
            CategoryRepository repository,
            CategoryMapper mapper,
            ImageLoader imageLoader) {

        return new CategoryServiceImpl(repository, mapper, imageLoader);
    }

    @Bean
    public ShopRepository shopRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcShopRepository(jdbcTemplate);
    }

    @Bean
    public ProductRepository productRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcProductRepository(jdbcTemplate);
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
