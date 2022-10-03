package com.marketplace.service.shop;

import com.marketplace.controller.shop.ShopInfo;

import java.util.List;

public interface ShopService {
    public void addShop(ShopInfo info);
    public List<Shop> getShops();
    public void removeShop(long shopId);
}
