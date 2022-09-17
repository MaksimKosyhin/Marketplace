package com.marketplace.order;

import com.marketplace.repository.app_analytics.AppAnalyticsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class JdbcOrderRepositoryTest {

    @Test
    void addOrder() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void getAllOrders() {
    }
}