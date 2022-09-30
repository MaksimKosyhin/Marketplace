package com.marketplace.category;

import com.marketplace.repository.category.*;
import com.marketplace.service.category.ProductQuery;
import com.marketplace.service.category.CategoryShop;
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
        assertThat(repository.getCategories(2)).isEqualTo(
                List.of(
                        new Category(3L, "laptops", 2L, "path2", false),
                        new Category(4L, "tablets", 2L, "path3", false)
                )
        );
    }

    @Test
    public void returnsCharacteristics() {
        assertThat(repository.getCharacteristics(3)).isEqualTo(
                List.of(
                        new Characteristic(1L, 3L, "color", "red"),
                        new Characteristic(2L, 3L, "color", "black")
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
        ProductInfo info1 = new ProductInfo(
                1,
                "laptopA",
                "path5",
                4,
                10,
                67
        );

        ProductInfo info2 = new ProductInfo(
                2,
                "laptopB",
                "path6",
                2,
                4,
                35
        );

        //then
        assertThat(repository.getProducts(List.of(1L, 2L))).isEqualTo(List.of(info1, info2));
    }

    @Test
    public void returnsProductsId() {

        ProductQuery query = new ProductQuery(
                3,
                List.of(1L),
                100
        );

        assertThat(repository.getProductsId(query)).isEqualTo(List.of(1L, 2L));
    }

    @Test
    public void returnsShops() {
        List<CategoryShop> expected = List.of(
                new CategoryShop(1, "shopA", "path8", true),
                new CategoryShop(2, "shopB", "path9", true),
                new CategoryShop(3,"shopC","path10",false)
        );

        assertThat(repository.getShops(3)).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void removesShopDependentEntities() {
        //when
        repository.removeShop(1);

        assertThat(template.queryForObject(
                "SELECT removed " +
                        "FROM shops " +
                        "WHERE shop_id = 1", Boolean.class))
                .isTrue();

        assertThat(template.queryForObject(
                "SELECT COUNT(*) " +
                        "FROM shop_products " +
                        "WHERE " +
                        "shop_id = 1 AND " +
                        "removed = FALSE", Long.class))
                .isZero();
    }
}
