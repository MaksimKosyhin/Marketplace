package com.marketplace.repository.app_analytics;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class JdbcAppAnalyticsRepository implements  AppAnalyticsRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAppAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        return  jdbcTemplate.queryForObject(sql, Integer.class, from, to);
    }

    @Override
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                    "category_id, " +
                    "name, " +
                    "img_location, " +
                    "categories.removed AS removed" +
                    "SUM((price * amount)) AS income " +
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

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<CategoryIncome>(CategoryIncome.class),
                from,
                to
        );
    }

    @Override
    public List<ProductIncome> getProductsIncome(ProductIncomeQuery productIncomeQuery) {
        String sql = "SELECT " +
                    "product_id, " +
                    "name, " +
                    "img_location, " +
                    "COUNT(order_id) AS number_of_orders, " +
                    "SUM((price * amount)) AS income " +
                    "removed " +
                "FROM products " +
                "INNER JOIN shop_products " +
                    "USING(product_id) " +
                "INNER JOIN order_shop_products " +
                    "USING(product_id) " +
                "WHERE " +
                    "category_id = ? AND " +
                    "registration_date > ? AND " +
                    "registration_date < ? " +
                "GROUP BY product_id";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<ProductIncome>(ProductIncome.class),
                productIncomeQuery.getCategoryId(),
                productIncomeQuery.getFrom(),
                productIncomeQuery.getTo()
        );
    }

    @Override
    public List<Shop> getShops(long categoryId) {
        String sql = "SELECT shop_id, name " +
                "FROM shops " +
                "INNER JOIN category_shops " +
                    "USING(shop_id) " +
                "WHERE category_id = ?";

        return jdbcTemplate.query(
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

        return jdbcTemplate.queryForObject(sql, Integer.class, from, to);
    }
}
