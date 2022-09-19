package com.marketplace.repository.order;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class JdbcOrderRepository implements OrderRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public long addOrder(long userId) {
        String updateLastOrder = "UPDATE orders " +
                "SET registration_date = CURRENT_DATE " +
                "WHERE " +
                    "user_id = ? AND " +
                    "order_id = (SELECT MAX(order_id) FROM orders)";
        jdbcTemplate.update(updateLastOrder, userId);

        String addOrder = "INSERT INTO orders(user_id) " +
                "VALUES(?) " +
                "RETURNING order_id";

        return jdbcTemplate.queryForObject(addOrder, Long.class, userId);
    }

    @Override
    public List<DbOrderedProduct> getOrder(long userId, long orderId) {
        String sql = "SELECT " +
                "orders.order_id AS order_id, " +
                "products.product_id AS product_id, " +
                "shops.shop_id AS shop_id, " +
                "products.name AS product_name, " +
                "products.img_location AS product_img_location, " +
                "amount, " +
                "price, " +
                "shops.name AS shop_name, " +
                "shops.img_location AS shop_img_location " +
                "FROM orders " +
                "INNER JOIN order_shop_products " +
                    "USING(order_id) " +
                "INNER JOIN shop_products " +
                    "USING(shop_id) " +
                "INNER JOIN products " +
                    "USING(product_id) " +
                "INNER JOIN shops " +
                    "USING(shop_id) " +
                "WHERE " +
                    "user_id = ? AND " +
                    "order_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<DbOrderedProduct>(DbOrderedProduct.class),
                userId,
                orderId
        );
    }

    @Override
    public List<DbOrderedProduct> getAllOrders(long userId) {
        String sql = "SELECT " +
                "orders.order_id AS order_id, " +
                "products.product_id AS product_id, " +
                "shops.shop_id AS shop_id, " +
                "registration_date, " +
                "products.name AS product_name, " +
                "products.img_location AS product_img_location, " +
                "amount, " +
                "price, " +
                "shops.name AS shop_name, " +
                "shops.img_location AS shop_img_location " +
                "FROM orders " +
                "INNER JOIN order_shop_products " +
                "USING(order_id) " +
                "INNER JOIN shop_products " +
                "USING(shop_id) " +
                "INNER JOIN products " +
                "USING(product_id) " +
                "INNER JOIN shops " +
                "USING(shop_id) " +
                "WHERE " +
                "user_id = ? AND " +
                "registration_date != NULL";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<DbOrderedProduct>(DbOrderedProduct.class),
                userId
        );
    }

    @Override
    public boolean addProductToOrder(OrderQuery orderQuery) {
        String sql = "INSERT INTO order_shop_products" +
                    "(order_id, " +
                    "product_id, " +
                    "shop_id, " +
                    "amount) " +
                "VALUES(?,?,?,?)";

        int updated = jdbcTemplate.update(
                sql,
                orderQuery.getOrderId(),
                orderQuery.getProductId(),
                orderQuery.getShopId(),
                orderQuery.getAmount()
        );

        return updated == 1;
    }

    @Override
    public boolean deleteProductFromOrder(OrderQuery orderQuery) {
        String sql = "DELETE FROM order_shop_products " +
                "WHERE " +
                "order_id = ? AND " +
                "product_id = ? AND " +
                "shop_id = ?";

        int updated = jdbcTemplate.update(
                sql,
                orderQuery.getOrderId(),
                orderQuery.getProductId(),
                orderQuery.getShopId()
        );

        return updated == 1;
    }

    @Override
    public boolean changeProductAmount(OrderQuery orderQuery) {
        String sql = "UPDATE order_shop_products " +
                "SET amount = ? " +
                "WHERE " +
                    "order_id = ? AND " +
                    "shop_id = ? AND " +
                    "product_id = ?";

        int updated = jdbcTemplate.update(
                sql,
                orderQuery.getOrderId(),
                orderQuery.getShopId(),
                orderQuery.getProductId()
        );

        return updated == 1;
    }

    @Override
    public long getCurrentOrderId(String username) {
        String sql = "SELECT MAX(order_id) " +
                "FROM orders " +
                "WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, getUserId(username));
    }

    @Override
    public long getUserId(String username) {
        String sql = "SELECT user_id " +
                "FROM users " +
                "WHERE username = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }
}
