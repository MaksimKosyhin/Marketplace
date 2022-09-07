package com.marketplace.category;

import org.assertj.core.api.Assertions;
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
class JdbcCategoryRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                    .withReuse(true);

    @Autowired
    JdbcCategoryRepositoryTest(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
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

    private void addCategoryForTest(Category category) {
        String sql = "INSERT INTO categories(name, parent_id, img_location) " +
                "VALUES(?, ?, ?)";
        jdbcTemplate.update(sql,
                category.getName(), category.getParentId(), category.getImgLocation());
        category.setCategoryId(this.queryLastCategoryId());
    }

    private void addProductForTest(String name, long categoryId) {
        String sql = "INSERT INTO products(name, category_id, img_location) " +
                "VALUES(?, ?, '')";
        jdbcTemplate.update(sql, name, categoryId);
    }

    private void addCharacteristicForTest(Characteristic characteristic) {
        String sql = "INSERT INTO characteristics (category_id, name, characteristic_value) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                characteristic.getCategoryId(),
                characteristic.getName(),
                characteristic.getCharacteristicValue());

        characteristic.setCharacteristicId(jdbcTemplate.queryForObject(
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
        Category category = new Category(
                -1L,
                "electronics",
                null,
                "dir1");
        addCategoryForTest(category);

        //then
        assertThat(categoryRepository.isParentCategory(category.getCategoryId())).isFalse();
    }

    @Test
    void isParentCategoryIfHasSubcategories() {
        //given
        Category parent = new Category(
                -1L,
                "electronics",
                null,
                "dir1");
        addCategoryForTest(parent);

        Category child = new Category(
                -1L,
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
        Category electronics = new Category(
                -1L,
                "electronics",
                null,
                "dir1");
        addCategoryForTest(electronics);

        Category laptops = new Category(
                -1L,
                "laptops",
                electronics.getCategoryId(),
                "dir2");
        addCategoryForTest(laptops);

        Category tablets = new Category(
                -1L,
                "tablets",
                electronics.getCategoryId(),
                "dir3");
        addCategoryForTest(tablets);

        Category smartphones = new Category(
                -1L,
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
        Category category = new Category(
                -1L,
                "electronics",
                null,
                "dir");

        //when
        categoryRepository.addCategory(category);
        category.setCategoryId(queryLastCategoryId());

        //then
        assertThat(
                jdbcTemplate.queryForObject(
                        "SELECT category_id, name, parent_id, img_location FROM categories",
                        new BeanPropertyRowMapper<Category>(Category.class)))
                .isEqualTo(category);
    }

    @Test
    void addsCharacteristic() {
        //given
        Category laptops = new Category(
                -1L,
                "laptops",
                null,
                "dir2");
        addCategoryForTest(laptops);

        List<Characteristic> characteristic = List.of(
                new Characteristic(
                        -1L,
                        laptops.getCategoryId(),
                        "color",
                        "grey"));

        characteristic.get(0).setCharacteristicId(jdbcTemplate.queryForObject(
                "SELECT MAX(characteristic_id) FROM characteristics",
                Long.class));

        //when
        categoryRepository.addCharacteristic(characteristic.get(0));

        //then
        assertThat(jdbcTemplate.query(
                "SELECT category_id, name, characteristic_value FROM characteristics",
                new BeanPropertyRowMapper<Characteristic>(Characteristic.class)))
                .isEqualTo(characteristic);
    }

    @Test
    void returnsCharacteristicsOfAllProductsInCategory() {
        //given
        Category laptops = new Category(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        Category pants = new Category(
                -1L,
                "pants",
                null,
                "dir2"
        );
        addCategoryForTest(pants);

        Characteristic diagonal = new Characteristic(
                -1L,
                laptops.getCategoryId(),
                "diagonal",
                "15.6"
        );
        addCharacteristicForTest(diagonal);

        Characteristic operativeMemory = new Characteristic(
                -1L,
                laptops.getCategoryId(),
                "operative memory",
                "32"
        );
        addCharacteristicForTest(operativeMemory);

        Characteristic size = new Characteristic(
                -1L,
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
        Category laptops = new Category(
                -1L,
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
        Category laptops = new Category(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        Category tablets = new Category(
                -1L,
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
        Category laptops = new Category(
                -1L,
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
        Category laptops = new Category(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        Category tablets = new Category(
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

        addShopForTest("shop for laptops");
        long shopForLaptops = queryLastShopId();
        addShopForTest("shop for tablets");
        long shopForTablets = queryLastShopId();

        ShopProduct shopForLaptopA = new ShopProduct(
                shopForLaptops,
                laptopA,
                -1,
                -1,
                -1
        );
        addShopProductForTest(shopForLaptopA);

        ShopProduct shopForLaptopB = new ShopProduct(
                shopForLaptops,
                laptopB,
                -1,
                -1,
                -1
        );
        addShopProductForTest(shopForLaptopB);

        ShopProduct shopForTabletC = new ShopProduct(
                shopForTablets,
                tabletC,
                -1,
                -1,
                -1
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
        Category laptops = new Category(
                -1L,
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
                -1,
                -1,
                -1
        );
        addShopProductForTest(shopForLaptopA);

        ShopProduct shopForLaptopB = new ShopProduct(
                shopForLaptops,
                laptopB,
                -1,
                -1,
                -1
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
        Category laptops = new Category(
                -1L,
                "laptops",
                null,
                "dir1"
        );
        addCategoryForTest(laptops);

        Category tablets = new Category(
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
        Category laptops = new Category(
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
                5,
                10,
                15
        );
        addShopProductForTest(firstShopForLaptopA);

        ShopProduct secondShopForLaptopA = new ShopProduct(
                secondShopForLaptops,
                laptopA,
                4,
                12,
                9
        );
        addShopProductForTest(secondShopForLaptopA);

        Characteristic color = new Characteristic(
                -1L,
                laptops.getCategoryId(),
                "color",
                "black"
        );
        addCharacteristicForTest(color);

        addProductCharacteristicForTest(laptopA, color.getCharacteristicId());

        List<ProductInfo> products = List.of(
                new ProductInfo(
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