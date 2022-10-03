package com.marketplace.app_analytics;

import com.marketplace.repository.app_analytics.AppAnalyticsRepository;
import com.marketplace.repository.app_analytics.CategoryIncome;
import com.marketplace.repository.app_analytics.ProductIncomeQuery;
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
public class JdbcAppAnalyticsRepositoryTest {
    private final JdbcTemplate template;
    private final AppAnalyticsRepository repository;
    private final Flyway flyway;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                    .withReuse(true);

    @Autowired
    JdbcAppAnalyticsRepositoryTest(JdbcTemplate template, AppAnalyticsRepository repository, Flyway flyway) {
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
    public void allMethodsExecuteWithoutCompileErrors() {
        repository.getTotalIncome(LocalDate.MIN, LocalDate.MIN);
        repository.getCategoriesIncome(LocalDate.MIN, LocalDate.MIN);
        repository.getNumberOfUsers(LocalDate.MIN, LocalDate.MIN);
        repository.getProductsIncomeForAllShops(new ProductIncomeQuery());
        repository.getProductsIncomeForShop(new ProductIncomeQuery());
        repository.getShops(1);

//        assertThat(template.queryForObject("SELECT COUNT(*) FROM categories", Long.class)).isEqualTo(0);
    }

    @Test
    public void returnsTotalIncome() {
        assertThat(repository.getTotalIncome(LocalDate.MIN, LocalDate.MAX)).isEqualTo(153);
    }

    @Test
    public void returnsCategoriesIncome() {
        assertThat(repository.getCategoriesIncome(LocalDate.MIN, LocalDate.MAX)).isEqualTo(
                List.of(
                        new CategoryIncome(3, "laptops", "path2", 153, false),
                        new CategoryIncome(4, "tablets", "path3", 0, false),
                        new CategoryIncome(5, "phones", "path4", 0, true)
                )
        );
    }
}
