package com.example.demo.category;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@Testcontainers
public class CategoryRepositoryTests {

    @Container
    private static PostgreSQLContainer container =
        new PostgreSQLContainer("postgres")
                .withDatabaseName("marketplace")
                .withUsername("postgres")
                .withPassword("pass");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

    @Test
    public void test() {

    }
}
