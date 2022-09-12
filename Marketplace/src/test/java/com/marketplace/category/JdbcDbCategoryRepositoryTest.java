package com.marketplace.category;

import com.marketplace.repository.product.ShopProduct;
import com.marketplace.repository.category.*;
import com.marketplace.service.category.SortingOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcDbCategoryRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
                    .withReuse(true);

    @Autowired
    JdbcDbCategoryRepositoryTest(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRepository = categoryRepository;
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
    public static  void closeContainer() {container.close();}


    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE categories, users, shops CASCADE");
    }

    private long queryLastCategoryId() {
        return jdbcTemplate
                .queryForObject("SELECT MAX(category_id) FROM categories", Long.class);
    }

    private long queryLastProductId() {
        return jdbcTemplate
                .queryForObject("SELECT MAX(product_id) FROM products", Long.class);
    }

    private long queryLastShopId() {
        return jdbcTemplate
                .queryForObject("SELECT MAX(shop_id) FROM shops", Long.class);
    }

    private void addCategoryForTest(DbCategory dbCategory) {
        String sql = "INSERT INTO categories(name, parent_id, img_location) " +
                "VALUES(?, ?, ?)";
        jdbcTemplate.update(sql,
                dbCategory.getName(), dbCategory.getParentId(), dbCategory.getImgLocation());
        dbCategory.setCategoryId(this.queryLastCategoryId());
    }

    private void addProductForTest(String name, long categoryId) {
        String sql = "INSERT INTO products(name, category_id, img_location) " +
                "VALUES(?, ?, '')";
        jdbcTemplate.update(sql, name, categoryId);
    }

    private void addCharacteristicForTest(DbCharacteristic dbCharacteristic) {
        String sql = "INSERT INTO characteristics (category_id, name, characteristic_value) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                dbCharacteristic.getCategoryId(),
                dbCharacteristic.getName(),
                dbCharacteristic.getCharacteristicValue());

        dbCharacteristic.setCharacteristicId(jdbcTemplate.queryForObject(
                "SELECT MAX(characteristic_id) FROM characteristics",
                Long.class));
    }

    private void addProductCharacteristicForTest(long productId, long characteristicId) {
        jdbcTemplate.update("INSERT INTO product_characteristics(product_id, characteristic_id) " +
                "VALUES(?, ?)", productId, characteristicId);
    }

    private void addShopForTest(String name) {
        String sql = "INSERT INTO shops(name, link, img_location) " +
                "VALUES(?, '', '')";

        jdbcTemplate.update(sql, name);
    }

    private void addShopProductForTest(ShopProduct shopProduct) {
        String sql = "INSERT INTO shop_products(shop_id, product_id, score, price, reviews) " +
                "VALUES(?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, shopProduct.getShopId(),
                shopProduct.getProductId(),
                shopProduct.getScore(),
                shopProduct.getPrice(),
                shopProduct.getReviews());
    }

    private boolean isCategoryRemoved(long categoryId) {
        return jdbcTemplate.queryForObject(
                "SELECT removed FROM categories WHERE category_id = ?",
                Boolean.class,
                categoryId);
    }

    private boolean isProductRemoved(long productId) {
        return jdbcTemplate.queryForObject(
                "SELECT removed FROM products WHERE product_id = ?",
                Boolean.class,
                productId);
    }

    private boolean isShopProductRemoved(long shopId, long productId) {
        return jdbcTemplate.queryForObject(
                "SELECT removed FROM shop_products WHERE shop_id = ? AND product_id = ?",
                Boolean.class,
                shopId,
                productId);
    }

    @Test
    void isNotParentCategoryWithoutSubcategories() {
        //given
        DbCategory dbCategory = new DbCategory(
                "electronics",
                null,
                "dir1");
        addCategoryForTest(dbCategory);

        //then
        assertThat(categoryRepository.isParentCategory(dbCategory.getCategoryId())).isFalse();
    }

    @Test
    void isParentCategoryIfHasSubcategories() {
        //given
        DbCategory parent = new DbCategory(
                "electronics",
                null,
                "dir1");
        addCategoryForTest(parent);

        DbCategory child = new DbCategory(
                "laptops",
                parent.getCategoryId(),
                "dir2");
        addCategoryForTest(child);

        //then
        assertThat(categoryRepository.isParentCategory(parent.getCategoryId())).isTrue();
    }

    @Test
    void returnsSubcategoriesOfParentCategory() {
        //given
        DbCategory electronics = new DbCategory(
                "electronics",
                null,
                "dir1");
        addCategoryForTest(electronics);

        DbCategory laptops = new DbCategory(
                "laptops",
                electronics.getCategoryId(),
                "dir2");
        addCategoryForTest(laptops);

        DbCategory tablets = new DbCategory(
                "tablets",
                electronics.getCategoryId(),
                "dir3");
        addCategoryForTest(tablets);

        DbCategory smartphones = new DbCategory(
                "smartphones",
                electronics.getCategoryId(),
                "dir4");
        addCategoryForTest(smartphones);

        jdbcTemplate.update("UPDATE categories " +
                        "SET removed = TRUE " +
                        "WHERE category_id = ?",
                smartphones.getCategoryId());

        //then
        assertThat(categoryRepository.getCategories(electronics.getCategoryId()))
                .isEqualTo(List.of(laptops, tablets));
    }

    @Test
    void addsCategory() {
        //given
        DbCategory dbCategory = new DbCategory(
                "electronics",
                null,
                "dir");

        //when
        categoryRepository.addCategory(dbCategory);
        dbCategory.setCategoryId(queryLastCategoryId());

        //then
        assertThat(
                jdbcTemplate.queryForObject(
                        "SELECT category_id, name, parent_id, img_location FROM categories",
                        new BeanPropertyRowMapper<DbCategory>(DbCategory.class)))
                .isEqualTo(dbCategory);
    }

    @Test
    void addsCharacteristic() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir2");
        addCategoryForTest(laptops);

        List<DbCharacteristic> dbCharacteristic = List.of(
                new DbCharacteristic(
                        laptops.getCategoryId(),
                        "color",
                        "grey"));

        dbCharacteristic.get(0).setCharacteristicId(jdbcTemplate.queryForObject(
                "SELECT MAX(characteristic_id) FROM characteristics",
                Long.class));

        //when
        categoryRepository.addCharacteristic(dbCharacteristic.get(0));

        //then
        assertThat(jdbcTemplate.query(
                "SELECT category_id, name, characteristic_value FROM characteristics",
                new BeanPropertyRowMapper<DbCharacteristic>(DbCharacteristic.class)))
                .isEqualTo(dbCharacteristic);
    }

    @Test
    void returnsCharacteristicsOfAllProductsInCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        DbCategory pants = new DbCategory(
                "pants",
                null,
                "dir2"
        );
        addCategoryForTest(pants);

        DbCharacteristic diagonal = new DbCharacteristic(
                laptops.getCategoryId(),
                "diagonal",
                "15.6"
        );
        addCharacteristicForTest(diagonal);

        DbCharacteristic operativeMemory = new DbCharacteristic(
                laptops.getCategoryId(),
                "operative memory",
                "32"
        );
        addCharacteristicForTest(operativeMemory);

        DbCharacteristic size = new DbCharacteristic(
                pants.getCategoryId(),
                "size",
                "XL"
        );
        addCharacteristicForTest(size);

        //then
        Assertions.assertThat(categoryRepository.getCharacteristics(laptops.getCategoryId()))
                .isEqualTo(List.of(diagonal, operativeMemory));
    }

    @Test
    void removesCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isCategoryRemoved(laptops.getCategoryId())).isTrue();
    }

    @Test
    void removesOnlyChosenCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        DbCategory tablets = new DbCategory(
                "tablets",
                null,
                "dir2"
        );
        addCategoryForTest(tablets);

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isCategoryRemoved(laptops.getCategoryId())).isTrue();
        assertThat(isCategoryRemoved(tablets.getCategoryId())).isFalse();
    }

    @Test
    void removesProductsOfRemovedCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        addProductForTest("laptopA", laptops.getCategoryId());
        long laptopA = queryLastProductId();
        addProductForTest("laptopB", laptops.getCategoryId());
        long laptopB = queryLastProductId();

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isProductRemoved(laptopA)).isTrue();
        assertThat(isProductRemoved(laptopB)).isTrue();
    }

    @Test
    void removesOnlyShopProductsOfRemovedCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        DbCategory tablets = new DbCategory(
                "tablets",
                null,
                "dir2"
        );
        addCategoryForTest(tablets);

        addProductForTest("laptopA", laptops.getCategoryId());
        long laptopA = queryLastProductId();
        addProductForTest("laptopB", laptops.getCategoryId());
        long laptopB = queryLastProductId();
        addProductForTest("tabletC", tablets.getCategoryId());
        long tabletC = queryLastProductId();

        addShopForTest("shop for laptops");
        long shopForLaptops = queryLastShopId();
        addShopForTest("shop for tablets");
        long shopForTablets = queryLastShopId();

        ShopProduct shopForLaptopA = new ShopProduct(
                shopForLaptops,
                laptopA,
                "", "", "",-1, -1, -1
        );
        addShopProductForTest(shopForLaptopA);

        ShopProduct shopForLaptopB = new ShopProduct(
                shopForLaptops,
                laptopB,
                "", "", "",-1, -1, -1
        );
        addShopProductForTest(shopForLaptopB);

        ShopProduct shopForTabletC = new ShopProduct(
                shopForTablets,
                tabletC,
                "", "", "",-1, -1, -1
        );
        addShopProductForTest(shopForTabletC);

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isShopProductRemoved(shopForTablets, tabletC)).isFalse();
    }

    @Test
    void removesShopProductsOfRemovedCategory() {
        //given
        DbCategory laptops = new DbCategory(
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        addProductForTest("laptopA", laptops.getCategoryId());
        long laptopA = queryLastProductId();
        addProductForTest("laptopB", laptops.getCategoryId());
        long laptopB = queryLastProductId();

        addShopForTest("shop for laptops");
        long shopForLaptops = queryLastShopId();

        ShopProduct shopForLaptopA = new ShopProduct(
                shopForLaptops,
                laptopA,
                "", "", "",-1, -1, -1
        );
        addShopProductForTest(shopForLaptopA);

        ShopProduct shopForLaptopB = new ShopProduct(
                shopForLaptops,
                laptopB,
                "", "", "",-1, -1, -1
        );
        addShopProductForTest(shopForLaptopB);

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isShopProductRemoved(shopForLaptops, laptopA)).isTrue();
        assertThat(isShopProductRemoved(shopForLaptops, laptopB)).isTrue();
    }

    @Test
    void removesOnlyProductsOfRemovedCategory() {
        //given
        DbCategory laptops = new DbCategory(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        DbCategory tablets = new DbCategory(
                -1L,
                "tablets",
                null,
                "dir2"
        );
        addCategoryForTest(tablets);

        addProductForTest("laptopA", laptops.getCategoryId());
        long laptopA = queryLastProductId();
        addProductForTest("laptopB", laptops.getCategoryId());
        long laptopB = queryLastProductId();

        addProductForTest("tabletC", tablets.getCategoryId());
        long tabletC = queryLastProductId();

        //when
        categoryRepository.removeCategory(laptops.getCategoryId());

        //then
        assertThat(isProductRemoved(tabletC)).isFalse();
    }

    @Test
    public void getProductsExecutes() {
        categoryRepository.getProducts(
                new ProductQuery(-1,
                        new long[] {-1, -2},
                        SortingOption.PRICE,
                        -1,
                        0,
                        true
                ));
    }

    @Test
    void returnsProductsOfCategory() {
        DbCategory laptops = new DbCategory(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        addProductForTest("laptopA", laptops.getCategoryId());
        long laptopA = queryLastProductId();

        addShopForTest("first shop for laptops");
        long firstShopForLaptops = queryLastShopId();
        addShopForTest("second shop for laptops");
        long secondShopForLaptops = queryLastShopId();

        ShopProduct firstShopForLaptopA = new ShopProduct(
                firstShopForLaptops,
                laptopA,
                "", "", "",-1, 10, 16
        );
        addShopProductForTest(firstShopForLaptopA);

        ShopProduct secondShopForLaptopA = new ShopProduct(
                secondShopForLaptops,
                laptopA,
                "", "", "",-1, 12, 8
        );
        addShopProductForTest(secondShopForLaptopA);

        DbCharacteristic color = new DbCharacteristic(
                -1L,
                laptops.getCategoryId(),
                "color",
                "black"
        );
        addCharacteristicForTest(color);

        addProductCharacteristicForTest(laptopA, color.getCharacteristicId());

        List<DbProductInfo> products = List.of(
                new DbProductInfo(
                        laptopA,
                        "laptopA",
                        "",
                        10,
                        12,
                        24
                )
        );

        //then
        assertThat(categoryRepository.getProducts(new ProductQuery(
                laptops.getCategoryId(),
                new long[] {color.getCharacteristicId()},
                SortingOption.PRICE,
                0,
                11,
                true
        ))).isEqualTo(products);
    }
}