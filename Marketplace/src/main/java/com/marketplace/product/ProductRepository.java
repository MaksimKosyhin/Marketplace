package com.marketplace.product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    public Product getProduct(long productId);

    public List<ShopProduct> getShopProducts(long productId);

    public List<ProductCharacteristic> getProductCharacteristics(long productId);

    public long addProduct(Product product);

    public boolean addShopProduct(ShopProduct shopProduct);

    public long addShop(Shop shop);

    public boolean addCharacteristicToProduct(long productId, long characteristicId);

    public boolean removeProduct(long productId);

    public boolean removeShopProduct(long productId, long shopId);

    public boolean removeShop(long shopId);
}
