package com.marketplace.repository.app_analytics;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class JdbcAppAnalyticsRepository implements  AppAnalyticsRepository{

    private final JdbcTemplate template;

    public JdbcAppAnalyticsRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public boolean categoryExists(long categoryId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM categories " +
                "WHERE " +
                "category_id = ? " +
                "LIMIT 1)";

        return template.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        String sql = "SELECT parent_category " +
                "FROM categories " +
                "WHERE category_id = ?";

        return template.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public int getTotalIncome(LocalDate from, LocalDate to) {
        String sql = "SELECT SUM((price * amount)) AS income " +
                "FROM shop_products " +
                "INNER JOIN order_shop_products " +
                "USING(product_id) " +
                "INNER JOIN orders " +
                "USING(order_id) " +
                "WHERE " +
                "registration_date > ? AND " +
                "registration_date < ?";

        Integer sum = template.queryForObject(sql, Integer.class, from, to);

        return sum == null ? 0 : sum;
    }

    @Override
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                "categories.category_id AS category_id, " +
                "categories.name AS name, " +
                "categories.img_location AS img_location, " +
                "categories.removed AS removed, " +
                "SUM(price * amount) AS income " +
                "FROM categories " +
                "INNER JOIN products " +
                "USING(category_id) " +
                "INNER JOIN shop_products " +
                "USING(product_id) " +
                "INNER JOIN order_shop_products " +
                "USING(product_id) " +
                "INNER JOIN orders " +
                "USING(order_id)" +
                "WHERE " +
                    "registration_date > ? AND " +
                    "registration_date < ?  " +
                "GROUP BY category_id";

        return template.query(
                sql,
                new BeanPropertyRowMapper<CategoryIncome>(CategoryIncome.class),
                from,
                to
        );
    }

    @Override
    public List<ProductIncome> getProductsIncomeForAllShops(ProductIncomeQuery query) {
        String sql = "SELECT " +
                "product_id, " +
                "name, " +
                "img_location, " +
                "COUNT(order_id) AS number_of_orders, " +
                "SUM(price * amount) AS income, " +
                "products.removed AS removed " +
                "FROM products " +
                "INNER JOIN shop_products " +
                "USING(product_id) " +
                "INNER JOIN order_shop_products " +
                "USING(product_id) " +
                "INNER JOIN orders " +
                "USING(order_id)" +
                "WHERE " +
                "category_id = ? AND " +
                "registration_date > ? AND " +
                "registration_date < ? " +
                "GROUP BY product_id";

        return template.query(
                sql,
                new BeanPropertyRowMapper<ProductIncome>(ProductIncome.class),
                query.getCategoryId(),
                query.getFrom(),
                query.getTo()
        );
    }

    @Override
    public List<ProductIncome> getProductsIncomeForShop(ProductIncomeQuery query) {
        String sql = "SELECT " +
                "product_id, " +
                "name, " +
                "img_location, " +
                "COUNT(order_id) AS number_of_orders, " +
                "SUM(price * amount) AS income, " +
                "products.removed AS removed " +
                "FROM products " +
                "INNER JOIN shop_products " +
                "USING(product_id) " +
                "INNER JOIN order_shop_products " +
                "USING(product_id) " +
                "INNER JOIN orders " +
                "USING(order_id)" +
                "WHERE " +
                "category_id = ? AND " +
                "registration_date > ? AND " +
                "registration_date < ? AND " +
                "shop_products.shop_id = ? " +
                "GROUP BY product_id";

        return template.query(
                sql,
                new BeanPropertyRowMapper<ProductIncome>(ProductIncome.class),
                query.getCategoryId(),
                query.getFrom(),
                query.getTo(),
                query.getShopId()
        );
    }

    @Override
    public List<Shop> getShops(long categoryId) {
        String sql = "SELECT shop_id, name " +
                "FROM shops " +
                "INNER JOIN category_shops " +
                "USING(shop_id) " +
                "WHERE category_id = ?";

        return template.query(
                sql,
                new BeanPropertyRowMapper<Shop>(Shop.class),
                categoryId
        );
    }

    @Override
    public int getNumberOfUsers(LocalDate from, LocalDate to) {
        String sql = "SELECT COUNT(*) " +
                "FROM users " +
                "WHERE " +
                    "registration_date > ? AND " +
                    "registration_date < ?";

        return template.queryForObject(sql, Integer.class, from, to);
    }
}
