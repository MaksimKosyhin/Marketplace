package com.marketplace.repository.shop;

import com.marketplace.service.shop.Shop;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcShopRepository implements ShopRepository{
    private final JdbcTemplate template;

    public JdbcShopRepository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public long addShop(Shop shop) {
        String sql = "INSERT INTO shops(name, img_location) " +
                "VALUES(?,?) " +
                "RETURNING shop_id";

        return template.queryForObject(sql, Long.class, shop.getName(), shop.getImgLocation());
    }

    @Override
    public List<Shop> getShops() {
        String sql = "SELECT " +
                    "shop_id, " +
                    "name, " +
                    "img_location " +
                "FROM shops " +
                "WHERE removed = FALSE";

        return template.query(sql, new BeanPropertyRowMapper<Shop>(Shop.class));
    }

    @Override
    public boolean removeShop(long shopId) {
        String sql = "UPDATE shops " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";

        return 1 == template.update(sql, shopId);
    }
}
