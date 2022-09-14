package com.marketplace.product;

import com.marketplace.repository.category.DbCharacteristic;
import com.marketplace.repository.product.ProductCharacteristic;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.repository.product.ShopProduct;
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
class JdbcDbProductRepositoryTestDescriptionDescriptionInfo {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                    .withReuse(true);

    @Autowired
    JdbcDbProductRepositoryTestDescriptionDescriptionInfo(JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
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

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE categories, users, shops CASCADE");
    }

    private long addCategoryForTest(String name) {
        String insertCategory = "INSERT INTO categories(name, parent_id, img_location) " +
                "VALUES(?, NULL, '')";
        jdbcTemplate.update(insertCategory, name);

        String lastCategoryId = "SELECT MAX(category_id) FROM categories";
        return jdbcTemplate.queryForObject(lastCategoryId, Long.class);
    }

    private long addProductForTest(long categoryId, String name) {
        String insertProduct = "INSERT INTO products(name, category_id, img_location) " +
                "VALUES(?,?,'')";
        jdbcTemplate.update(insertProduct, name, categoryId);

        String lastProductId = "SELECT MAX(product_id) FROM products";
        return jdbcTemplate.queryForObject(lastProductId, Long.class);
    }

    private long addShopForTest(String name) {
        String insertProduct = "INSERT INTO shops(name, link, img_location) " +
                "VALUES(?,'','')";
        jdbcTemplate.update(insertProduct, name);

        String lastShopId = "SELECT MAX(shop_id) FROM shops";
        return jdbcTemplate.queryForObject(lastShopId, Long.class);
    }

    private void addShopProductForTest(ShopProduct shopProduct) {
        String insertShopProduct = "INSERT INTO shop_products(shop_id, product_id, score, price, reviews) " +
                "VALUES(?,?,?,?,?)";
        jdbcTemplate.update(
                insertShopProduct,
                shopProduct.getShopId(),
                shopProduct.getProductId(),
                shopProduct.getScore(),
                shopProduct.getPrice(),
                shopProduct.getReviews()
        );

        shopProduct.setProductId(0);
        shopProduct.setShopId(0);
    }

    private long addCharacteristicForTest(DbCharacteristic dbCharacteristic) {
        String sql = "INSERT INTO characteristics(category_id, name, characteristic_value) " +
                "VALUES(?,?,?)";
        jdbcTemplate.update(
                sql,
                dbCharacteristic.getCategoryId(),
                dbCharacteristic.getName(),
                dbCharacteristic.getCharacteristicValue());

        return jdbcTemplate.queryForObject(
                "SELECT MAX(characteristic_id) FROM characteristics",
                Long.class
        );
    }

    private void addProductCharacteristicForTest(long productId, long characteristicId) {
        String sql = "INSERT INTO product_characteristics(product_id, characteristic_id) " +
                "VALUES(?,?)";
        jdbcTemplate.update(sql, productId, characteristicId);
    }

    @Test
    void returnsShopProductsOfProduct() {
        //given
        long fruits = addCategoryForTest("fruits");
        long apple = addProductForTest(fruits, "apple");
        long shopA = addShopForTest("shopA");
        long shopB = addShopForTest("shopB");

        ShopProduct appleInShopA = new ShopProduct(
                shopA, apple, "shopA", "", "", 3, 4, 5
        );
        addShopProductForTest(appleInShopA);

        ShopProduct appleInShopB = new ShopProduct(
                shopB, apple, "shopB", "", "", 3, 4, 5
        );
        addShopProductForTest(appleInShopB);

        List<ShopProduct> expected = List.of(appleInShopA, appleInShopB);

        //then
        assertThat(productRepository.getShopProducts(apple))
                .isEqualTo(expected);
    }

    @Test
    void returnsCharacteristicsOfProduct() {
        //given
        long fruits = addCategoryForTest("fruits");
        long apple = addProductForTest(fruits, "apple");
        long colorId = addCharacteristicForTest(
                new DbCharacteristic(fruits, "color", "green"));
        long weightId = addCharacteristicForTest(
                new DbCharacteristic(fruits, "weight", "50g"));

        addProductCharacteristicForTest(apple, colorId);
        addProductCharacteristicForTest(apple, weightId);

        ProductCharacteristic color =
                new ProductCharacteristic("color", "green");
        ProductCharacteristic weight =
                new ProductCharacteristic("weight", "50g");

        List<ProductCharacteristic> expected = List.of(color, weight);

        //then
        assertThat(productRepository.getProductCharacteristics(apple))
                .isEqualTo(expected);
    }
}