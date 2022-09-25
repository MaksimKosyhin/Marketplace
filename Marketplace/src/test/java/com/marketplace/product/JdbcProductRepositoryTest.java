package com.marketplace.product;

import com.marketplace.repository.product.Product;
import com.marketplace.repository.product.ProductCharacteristic;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.repository.product.ShopProduct;
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
class JdbcProductRepositoryTest {

    private final JdbcTemplate template;
    private final ProductRepository repository;
    private final Flyway flyway;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
                    .withReuse(true);

    @Autowired
    JdbcProductRepositoryTest(JdbcTemplate template, ProductRepository repository, Flyway flyway) {
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
   public void existingProductExists() {
        assertThat(repository.productExists(1)).isTrue();
   }

   @Test
   public void returnsProduct() {
        assertThat(repository.getProduct(1)).isEqualTo(
                new Product(1,0,"laptopA", "path5")
        );
   }

   @Test
   public void returnsShopProducts() {
        assertThat(repository.getShopProducts(1)).isEqualTo(
                List.of(
                        new ShopProduct(
                                0,
                                0,
                                "shopA",
                                "link1",
                                "path8",
                                3,
                                4,
                                2),
                        new ShopProduct(
                                0,
                                0,
                                "shopB",
                                "link4",
                                "path9",
                                3,
                                5,
                                50),
                        new ShopProduct(
                                0,
                                0,
                                "shopC",
                                "link7",
                                "path10",
                                4,
                                10,
                                15)
                )
        );
   }

   @Test
    public void returnsProductCharacteristics() {
        assertThat(repository.getProductCharacteristics(1)).isEqualTo(
                List.of(new ProductCharacteristic("color", "red"))
        );
   }

   @Test
    public void removesProductDependentEntities() {
       //when
       repository.removeProduct(1);

       //then
       assertThat(template.queryForObject(
                    "SELECT removed " +
                       "FROM products " +
                       "WHERE product_id = 1", Boolean.class))
               .isTrue();

       assertThat(template.queryForObject(
                    "SELECT COUNT(*) " +
                       "FROM shop_products " +
                       "WHERE " +
                       "product_id = 1 AND " +
                       "removed = FALSE", Long.class))
               .isZero();
   }

   @Test
    public void removesShopProduct() {
        //when
       repository.removeShopProduct(1, 1);

       //then
       assertThat(template.queryForObject(
               "SELECT removed " +
                       "FROM shop_products " +
                       "WHERE " +
                       "product_id = 1 AND " +
                       "shop_id = 1", Boolean.class)).isTrue();
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