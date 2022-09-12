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

    private int getCountOfShopUpdatedEntities(long shopId) {
        String sql = "SELECT COUNT(*) " +
                "FROM shop_products " +
                "WHERE shop_id = ?";

        return 1 + jdbcTemplate.queryForObject(sql, Integer.class, shopId);
    }

    private int getCountOfProductUpdatedEntities(long productId) {
        String sql = "SELECT COUNT(*) " +
                "FROM shop_products " +
                "WHERE product_id = ?";

        return 1 + jdbcTemplate.queryForObject(sql, Integer.class, productId);
    }

    @Override
    public Product getProduct(long productId) {
        String sql = "SELECT " +
                    "product_id, " +
                    "name, " +
                    "img_location " +
                "FROM products " +
                "WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, Product.class, productId);
    }

    @Override
    public List<ShopProduct> getShopProducts(long productId) {
        String sql = "SELECT " +
                    "name, " +
                    "link, " +
                    "img_location, " +
                    "score, " +
                    "price, " +
                    "reviews " +
                "FROM shops " +
                "INNER JOIN shop_products " +
                    "USING(shop_id)" +
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
        String sql = "SELECT " +
                    "name, " +
                    "characteristic_value " +
                "FROM characteristics " +
                "INNER JOIN product_characteristics " +
                    "USING(characteristic_id)" +
                "WHERE product_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<ProductCharacteristic>(ProductCharacteristic.class),
                productId
        );
    }

    @Override
    public long addProduct(Product product) {
        String sql = "INSERT INTO products(" +
                    "category_id, " +
                    "name, " +
                    "img_location) " +
                "VALUES(?,?,?) " +
                "RETURNING product_id";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                product.getCategory_id(),
                product.getName(),
                product.getImgLocation()
        );
    }

    @Override
    public boolean addShopProduct(ShopProduct shopProduct) {
        String sql = "INSERT INTO shop_products(" +
                    "shop_id, " +
                    "product_id, " +
                    "score, " +
                    "price, " +
                    "reviews) " +
                "VALUES(?,?,?,?,?)";

        int updated = jdbcTemplate.update(
                sql,
                shopProduct.getShopId(),
                shopProduct.getProductId(),
                shopProduct.getScore(),
                shopProduct.getPrice(),
                shopProduct.getReviews()
        );

        return updated == 1;
    }

    @Override
    public long addShop(Shop shop) {
        String sql = "INSERT INTO shops(" +
                    "name, " +
                    "link, " +
                    "img_location) " +
                "VALUES(?,?,?) " +
                "RETURNING shop_id";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                shop.getName(),
                shop.getLink(),
                shop.getImgLocation()
        );
    }

    @Override
    public boolean addCharacteristicToProduct(long productId, long characteristicId) {
        String sql = "INSERT INTO product_characteristics(" +
                    "product_id, " +
                    "characteristic_ic) " +
                "VALUES(?,?)";

        int updated = jdbcTemplate.update(sql, productId, characteristicId);

        return updated == 1;
    }

    @Transactional
    @Override
    public boolean removeProduct(long productId) {
        int updated = 0;

        String removeProduct = "UPDATE products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        updated += jdbcTemplate.update(removeProduct, productId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        updated += jdbcTemplate.update(removeShopProducts, productId);

        return updated == getCountOfProductUpdatedEntities(productId);
    }

    @Override
    public boolean removeShopProduct(long productId, long shopId) {
        String sql = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ? AND shop_id = ?";
        int updated = jdbcTemplate.update(sql, productId, shopId);

        return updated == 1;
    }

    @Transactional
    @Override
    public boolean removeShop(long shopId) {
        int updated = 0;

        String removeShop = "UPDATE shops " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";
        updated += jdbcTemplate.update(removeShop, shopId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";
        updated += jdbcTemplate.update(removeShopProducts, shopId);

        return updated == getCountOfShopUpdatedEntities(shopId);
    }
}