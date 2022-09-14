package com.marketplace.repository.product;

import java.util.List;

public interface ProductRepository {
    public boolean productExists(long productId);

    public DbProduct getProduct(long productId);

    public List<ShopProduct> getShopProducts(long productId);

    public List<ProductCharacteristic> getProductCharacteristics(long productId);

    public long addProduct(DbProduct dbProduct);

    public boolean addShopProduct(ShopProduct shopProduct);

    public long addShop(Shop shop);

    public boolean addCharacteristicToProduct(long productId, long characteristicId);

    public boolean removeProduct(long productId);

    public boolean removeShopProduct(long productId, long shopId);

    public boolean removeShop(long shopId);
}
