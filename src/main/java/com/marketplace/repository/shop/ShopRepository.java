package com.marketplace.repository.shop;

import com.marketplace.service.shop.Shop;

import java.util.List;

public interface ShopRepository {
    public long addShop(Shop shop);
    public List<Shop> getShops();
    public boolean removeShop(long shopId);
}
