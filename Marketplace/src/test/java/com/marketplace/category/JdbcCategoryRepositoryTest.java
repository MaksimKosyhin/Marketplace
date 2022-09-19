package com.marketplace.category;

import com.marketplace.repository.category.*;
import com.marketplace.service.category.SortingOption;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcCategoryRepositoryTest {

    private final JdbcTemplate template;
    private final CategoryRepository repository;
    private final Flyway flyway;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
                    .withReuse(true);

    @Autowired
    public JdbcCategoryRepositoryTest(JdbcTemplate template, CategoryRepository repository, Flyway flyway) {
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
    public void existingCategoryExists() {
        assertThat(repository.categoryExists(1)).isTrue();
    }

    @Test
    public void isParentCategoryForChild() {
        assertThat(repository.isParentCategory(1)).isTrue();
    }

    @Test
    public void returnsCategories() {
        assertThat(repository.getCategories(1)).isEqualTo(
                List.of(
                        new DbCategory(2L, "laptops", 1L, "path2"),
                        new DbCategory(3L, "tablets", 1L, "path3")
                )
        );
    }

    @Test
    public void returnsCharacteristics() {
        assertThat(repository.getCharacteristics(2)).isEqualTo(
                List.of(
                        new DbCharacteristic(1L, 2L, "color", "red"),
                        new DbCharacteristic(2L, 2L, "color", "black")
                )
        );
    }

    @Test
    public void removesAllCategoryDependentEntities() {
        //when
        repository.removeCategory(2);

        assertThat(template.queryForObject(
                    "SELECT removed " +
                        "FROM categories " +
                        "WHERE category_id = 2", Boolean.class))
                .isTrue();

        //then
        assertThat(template.queryForObject(
                    "SELECT COUNT(*) " +
                        "FROM products " +
                        "WHERE " +
                        "category_id = 2 AND " +
                        "removed = FALSE", Long.class))
                .isZero();

        assertThat(template.queryForObject(
                    "SELECT COUNT(*) " +
                        "FROM shop_products " +
                        "INNER JOIN products " +
                        "USING(product_id) " +
                        "WHERE category_id = 2 AND " +
                        "shop_products.removed = FALSE", Long.class))
                .isZero();
    }

    @Test
    public void returnsProducts() {
        //given
        DbProductInfo info1 = new DbProductInfo(
                1,
                "laptopA",
                "path5",
                4,
                10,
                67
        );

        DbProductInfo info2 = new DbProductInfo(
                2,
                "laptopB",
                "path6",
                2,
                4,
                35
        );

        ProductQuery query = new ProductQuery(
                2,
                new long[]{1},
                SortingOption.PRICE,
                0,
                100,
                true
        );

        //then
        assertThat(repository.getProducts(query)).isEqualTo(List.of(info1, info2));
    }
}
