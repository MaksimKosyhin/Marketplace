package com.marketplace.repository.category;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

public class JdbcCategoryRepository implements  CategoryRepository{

    private final JdbcTemplate jdbcTemplate;
    private final ColumnConverter columnConverter;

    public JdbcCategoryRepository(JdbcTemplate jdbcTemplate, ColumnConverter columnConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.columnConverter = columnConverter;
    }

    private String getParametersTemplate(int numberOfParameters) {
        return String.join(",", Collections.nCopies(numberOfParameters, "?"));
    }

    private String getOrderByStatement(ProductQuery productQuery) {
        return "ORDER BY " +
                columnConverter.getColumnName(productQuery.getSortingOption()) +
                (productQuery.isOrderDescending() ? " DESC " : " ASC ");
    }

    private long getCountOfCategoryUpdatedEntities(long categoryId) {
        int count = 0;

        String  countProducts = "SELECT COUNT(*) " +
                "FROM products " +
                "WHERE category_id = ? ";
        count += jdbcTemplate.queryForObject(countProducts, Integer.class, categoryId);

        String countShopProducts = "WITH updated_products AS (" +
                    "SELECT product_id " +
                    "FROM products " +
                "WHERE category_id = ?) " +
                "SELECT COUNT(*) " +
                "FROM shop_products " +
                "INNER JOIN updated_products " +
                "USING(product_id)";
        count += jdbcTemplate.queryForObject(countShopProducts, Integer.class, categoryId);

        return 1 + count;
    }

    @Override
    public boolean categoryExists(long categoryId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM categories " +
                "WHERE " +
                "category_id = ? AND " +
                "removed = FALSE " +
                "LIMIT 1)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM categories " +
                "WHERE parent_id = ? " +
                "LIMIT 1)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public List<DbCategory> getCategories(long parentId) {
        String sql = "SELECT " +
                "parent_id, " +
                "category_id, " +
                "name, " +
                "img_location " +
                "FROM categories " +
                "WHERE " +
                "parent_id = ? AND " +
                "removed = FALSE";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<DbCategory>(DbCategory.class),
                parentId
        );
    }

    @Override
    public List<DbProductInfo> getProducts(ProductQuery productQuery) {
        String sql = "SELECT " +
                "product_id, " +
                "products.name AS name, " +
                "products.img_location AS img_location, " +
                "MIN(price) AS min_price, " +
                "MAX(price) AS max_price, " +
                "SUM(reviews) AS total_reviews " +
                "FROM products " +
                "INNER JOIN shop_products " +
                "USING(product_id) " +
                "INNER JOIN shops " +
                    "USING(shop_id) " +
                "INNER JOIN (" +
                    "SELECT product_id " +
                    "FROM product_characteristics " +
                    "WHERE characteristic_id " +
                        "IN(" + getParametersTemplate
                        (productQuery.getNumberOfCharacteristics()) + ")) characteristics " +
                    "USING(product_id) " +
                "WHERE " +
                    "products.removed = FALSE AND " +
                    "shop_products.removed = FALSE AND " +
                    "shops.removed = FALSE AND " +
                    "category_id = ? AND " +
                    "product_id > ?" +
                "GROUP BY product_id " +
                getOrderByStatement(productQuery) +
                "LIMIT ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<DbProductInfo>(DbProductInfo.class),
                productQuery.getQueryParameters()
        );
    }

    @Override
    public long addCategory(DbCategory dbCategory) {
        String sql = "INSERT INTO categories(" +
                "name, " +
                "parent_id, " +
                "img_location) " +
                "VALUES (?, ?, ?) " +
                "RETURNING category_id";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    Long.class,
                    dbCategory.getName(),
                    dbCategory.getParentId(),
                    dbCategory.getImgLocation()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public long addCharacteristic(DbCharacteristic dbCharacteristic) {
        String sql = "INSERT INTO characteristics(" +
                "category_id, " +
                "name, " +
                "characteristic_value) " +
                "VALUES (?, ?, ?) " +
                "RETURNING characteristic_id";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    Long.class,
                    dbCharacteristic.getCategoryId(),
                    dbCharacteristic.getName(),
                    dbCharacteristic.getCharacteristicValue()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public List<DbCharacteristic> getCharacteristics(long categoryId) {
        String sql = "SELECT characteristic_id, category_id, name, characteristic_value " +
                "FROM characteristics " +
                "WHERE category_id = ?";

        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<DbCharacteristic>(DbCharacteristic.class),
                categoryId);
    }

    @Override
    @Transactional
    public boolean removeCategory(long categoryId) {
        int updated = 0;

        String removeCategory = "UPDATE categories " +
                "SET removed = TRUE " +
                "WHERE category_id = ?";
        updated += jdbcTemplate.update(removeCategory, categoryId);

        String removeProducts = "UPDATE products " +
                "SET removed = TRUE " +
                "WHERE category_id = ?";
        updated += jdbcTemplate.update(removeProducts, categoryId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE product_id IN(" +
                    "SELECT product_id " +
                    "FROM products " +
                    "WHERE removed = TRUE)";
        updated += jdbcTemplate.update(removeShopProducts);

        return updated == getCountOfCategoryUpdatedEntities(categoryId);
    }
}
