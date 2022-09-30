package com.marketplace.repository.product;

import com.marketplace.service.category.CategoryShop;
import com.marketplace.service.product.ShopProductInfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class JdbcProductRepository implements  ProductRepository{

    private final JdbcTemplate template;

    public JdbcProductRepository(JdbcTemplate template) {
        this.template = template;
    }

    private int getCountOfProductUpdatedEntities(long productId) {
        String sql = "SELECT COUNT(*) " +
                "FROM shop_products " +
                "WHERE product_id = ?";

        return 1 + template.queryForObject(sql, Integer.class, productId);
    }

    @Override
    public boolean productExists(long productId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM products " +
                "WHERE product_id = ? " +
                "LIMIT 1)";

        return template.queryForObject(sql, Boolean.class, productId);
    }

    @Override
    public long getCategoryId(long productId) {
        String sql = "SELECT category_id " +
                "FROM products " +
                "WHERE product_id = ?";

        return template.queryForObject(sql, Long.class, productId);
    }

    @Override
    public Product getProduct(long productId) {
        String sql = "SELECT " +
                "product_id, " +
                "name, " +
                "img_location " +
                "FROM products " +
                "WHERE product_id = ?";

        return template.queryForObject(
                sql,
                new BeanPropertyRowMapper<Product>(Product.class),
                productId
        );
    }

    @Override
    public List<CategoryShop> getShops() {
        String sql = "SELECT " +
                    "shop_id, " +
                    "name, " +
                    "img_location " +
                "FROM shops " +
                "WHERE removed = FALSE";

        return template.query(sql, new BeanPropertyRowMapper<CategoryShop>(CategoryShop.class));
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

        return template.query(
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

        return template.query(
                sql,
                new BeanPropertyRowMapper<ProductCharacteristic>(ProductCharacteristic.class),
                productId
        );
    }

    @Override
    public List<ProductCharacteristic> getCategoryCharacteristics(long categoryId) {
        String sql = "SELECT " +
                "characteristic_id, " +
                "name, " +
                "characteristic_value " +
                "FROM characteristics " +
                "WHERE category_id = ?";

        return template.query(sql,
                new BeanPropertyRowMapper<ProductCharacteristic>(ProductCharacteristic.class),
                categoryId);
    }

    @Override
    public long addProduct(Product product) {
        String sql = "INSERT INTO products(" +
                "category_id, " +
                "name, " +
                "img_location) " +
                "VALUES(?,?,?) " +
                "RETURNING product_id";

        try {
            return template.queryForObject(
                    sql,
                    Long.class,
                    product.getCategoryId(),
                    product.getName(),
                    product.getImgLocation()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public boolean addShopProduct(ShopProductInfo info) {
        String sql = "INSERT INTO shop_products(" +
                "shop_id, " +
                "product_id, " +
                "link, " +
                "score, " +
                "price, " +
                "reviews) " +
                "VALUES(?,?,?,?,?)";

        int updated = template.update(
                sql,
                info.getShopId(),
                info.getProductId(),
                info.getLink(),
                info.getScore(),
                info.getPrice(),
                info.getReviews()
        );

        return updated == 1;
    }

    @Override
    public boolean addCharacteristicToProduct(long productId, long characteristicId) {
        String sql = "INSERT INTO product_characteristics(" +
                    "product_id, " +
                    "characteristic_ic) " +
                "VALUES(?,?)";

        int updated = template.update(sql, productId, characteristicId);

        return updated == 1;
    }

    @Transactional
    @Override
    public boolean removeProduct(long productId) {
        int updated = 0;

        String removeProduct = "UPDATE products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        updated += template.update(removeProduct, productId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ?";
        updated += template.update(removeShopProducts, productId);

        return updated == getCountOfProductUpdatedEntities(productId);
    }

    @Override
    public boolean removeShopProduct(long productId, long shopId) {
        String sql = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id = ? AND shop_id = ?";
        int updated = template.update(sql, productId, shopId);

        return updated == 1;
    }
}
