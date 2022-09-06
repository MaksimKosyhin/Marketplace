package com.example.demo.category;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collections;
import java.util.List;

public class JdbcCategoryRepository implements  CategoryRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcCategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM categories WHERE parent_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public List<Category> getCategories(long parentId) {
        String sql = "SELECT parent_id, category_id, name, img_location " +
                "FROM categories " +
                "WHERE parent_id = ? AND removed = FALSE";

        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<Category>(Category.class),
                parentId);
    }

    @Override
    public List<ProductInfo> getProducts(ProductQuery productQuery) {
        String queryShops =
                "SELECT shop_id FROM shops WHERE removed = FALSE";

        String queryPrices =
                "SELECT product_id, score, price, reviews " +
                "FROM shop_products " +
                "INNER JOIN (" + queryShops + ") shops " +
                        "USING(shop_id) " +
                "WHERE removed = FALSE";

        String queryProductCharacteristics =
                "SELECT product_id " +
                "FROM product_characteristics " +
                "WHERE characteristic_id IN(" + productQuery
                        .getCharacteristicsInsertParametersTemplate() + ")";

        String queryProduct =
                "SELECT " +
                    "product_id, " +
                    "name, " +
                    "img_location, " +
                    "MIN(price) AS min_price, " +
                    "MAX(price) AS max_price, " +
                    "AVG(prices.score) AS average_score, " +
                    "SUM(reviews) AS total_reviews " +
                "FROM products " +
                "INNER JOIN (" + queryPrices + ") prices " +
                    "USING(product_id) " +
                "INNER JOIN (" + queryProductCharacteristics + ") characteristics " +
                    "USING(product_id) " +
                "WHERE " +
                    "removed = FALSE " +
                    "AND category_id = ? " +
                    "AND product_id BETWEEN ? AND ? " +
                "GROUP BY product_id " +
                productQuery.getOrderBy().getSql();

        return jdbcTemplate.query(queryProduct,
                new BeanPropertyRowMapper<ProductInfo>(ProductInfo.class),
                productQuery.getQueryParameters());
    }

    @Override
    public void addCategory(Category category) {
        String sql = "INSERT INTO categories (name, parent_id, img_location) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                category.getName(), category.getParentId(), category.getImgLocation());
    }

    @Override
    public void addCharacteristic(Characteristic characteristic) {
        String sql = "INSERT INTO characteristics (category_id, name, characteristic_value) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                characteristic.getCategoryId(),
                characteristic.getName(),
                characteristic.getCharacteristicValue());
    }

    @Override
    public List<Characteristic> getCharacteristics(long categoryId) {
        String sql = "SELECT characteristic_id, category_id, name, characteristic_value " +
                "FROM characteristics " +
                "WHERE category_id = ?";

        return  jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<Characteristic>(Characteristic.class),
                categoryId);
    }

    @Override
    public void removeCategory(long categoryId) {
        String removeCategory = "UPDATE categories SET removed = TRUE WHERE category_id = ?";
        jdbcTemplate.update(removeCategory, categoryId);

        String removeProducts = "UPDATE products SET removed = TRUE WHERE category_id = ?";
        jdbcTemplate.update(removeProducts, categoryId);

        String removeShopProducts = "UPDATE shop_products SET removed = TRUE WHERE product_id IN " +
                "(SELECT product_id FROM products WHERE removed = TRUE)";
        jdbcTemplate.update(removeShopProducts);
    }
}
