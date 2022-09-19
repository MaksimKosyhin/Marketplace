package com.marketplace.order;

import com.marketplace.repository.order.DbOrderedProduct;
import com.marketplace.repository.order.OrderRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcOrderRepositoryTest {
    private final JdbcTemplate template;
    private final OrderRepository repository;
    private final Flyway flyway;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
                    .withReuse(true);

    @Autowired
    public JdbcOrderRepositoryTest(JdbcTemplate template, OrderRepository repository, Flyway flyway) {
        this.template = template;
        this.repository = repository;
        this.flyway = flyway;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    public static void setUp() {
        container.start();
    }

    @AfterAll
    public static void close() {
        container.stop();
    }

    @AfterEach
    void tearDown() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void updatesLastOrderBeforeAddingNew() {
        //given
        long lastOrder = template.queryForObject(
                "SELECT MAX(order_id) " +
                        "FROM orders " +
                        "WHERE user_id = 1", Long.class);

        //when
        repository.addOrder(1);

        //then
        assertThat(template.queryForObject(
                "SELECT registration_date " +
                        "FROM orders " +
                        "WHERE order_id = ?", LocalDate.class, lastOrder))
                .isNotNull();
    }

    @Test
    public void returnProductsOfOrder() {
        assertThat(repository.getOrder( 1)).isEqualTo(
                List.of(
                        new DbOrderedProduct(
                                1,
                                1,
                                1,
                                null,
                                "laptopA",
                                "path5",
                                12,
                                12,
                                "shopA",
                                "path8"),
                        new DbOrderedProduct(
                                1,
                                1,
                                2,
                                null,
                                "laptopB",
                                "path6",
                                3,
                                12,
                                "shopA",
                                "path8"),
                        new DbOrderedProduct(
                                1,
                                2,
                                3,
                                null,
                                "laptopC",
                                "path7",
                                4,
                                12,
                                "shopB",
                                "path9")
                )
        );
    }
}
