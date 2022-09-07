package com.marketplace.product;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class JdbcProductRepository implements  ProductRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private long getLastProductId() {
        return jdbcTemplate
                .queryForObject("SELECT MAX(product_id) FROM products", Long.class);
    }

    private long getLastShopId() {
        return jdbcTemplate
                .queryForObject("SELECT MAX(shop_id) FROM shops", Long.class);
    }

    @Override
    public Product getProduct(long productId) {
        String sql = "SELECT product_id, name, img_location " +
                "FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, Product.class, productId);
    }

    @Override
    public List<ShopProduct> getShopProducts(long productId) {
        String sql = "SELECT name, link, img_location, score, price, reviews " +
                "FROM shops " +
                "INNER JOIN shop_products " +
                    "USING shop_id " +
                "WHERE " +
                    "product_id = ? AND " +
                    "shops.removed = FALSE AND " +
                    "shop_products.removed = FALSE";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<ShopProduct>(ShopProduct.class),
                productId
        );
    }

    @Override
    public List<ProductCharacteristic> getProductCharacteristics(long productId) {
        String sql = "SELECT name, characteristic_value " +
                "FROM characteristics " +
                "INNER JOIN product_characteristics " +
                    "ON characteristic_id " +
                "WHERE product_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<ProductCharacteristic>(ProductCharacteristic.class),
                productId
        );
    }

    @Override
    public long addProduct(Product product) {
        String sql = "INSERT INTO products(category_id, name, img_location) " +
                "VALUES(?,?,?)";

        jdbcTemplate.update(
                sql,
                product.getCategory_id(),
                product.getName(),
                product.getImgLocation()
        );

        return  getLastProductId();
    }

    @Override
    public Map<String, Long> addShopProduct(ShopProduct shopProduct) {
        String sql = "INSERT INTO shop_products(shop_id, product_id, score, price, reviews) " +
                "VALUES(?,?,?,?,?)";

        jdbcTemplate.update(
                sql,
                shopProduct.getShopId(),
                shopProduct.getProductId(),
                shopProduct.getScore(),
                shopProduct.getPrice(),
                shopProduct.getReviews()
        );

        return  Map.of(
                "productId", shopProduct.getProductId(),
                "shopId", shopProduct.getShopId()
        );
    }

    @Override
    public long addShop(Shop shop) {
        String sql = "INSERT INTO shops(name, link, img_location) " +
                "VALUES(?,?,?)";

        jdbcTemplate.update(
                sql,
                shop.getName(),
                shop.getLink(),
                shop.getImgLocation()
        );

        return getLastShopId();
    }

    @Transactional
    @Override
    public boolean removeProduct(long productId) {
        String removeProduct = "UPDATE products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        jdbcTemplate.update(removeProduct, productId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        jdbcTemplate.update(removeShopProducts, productId);

        return true;
    }

    @Override
    public boolean removeShopProduct(long productId, long shopId) {
        String sql = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ? AND shop_id = ?";
        jdbcTemplate.update(sql, productId, shopId);

        return true;
    }

    @Transactional
    @Override
    public boolean removeShop(long shopId) {
        String removeShop = "UPDATE shops " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";
        jdbcTemplate.update(removeShop, shopId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        jdbcTemplate.update(removeShopProducts, shopId);

        return true;
    }
}
