package com.marketplace.repository.product;

import com.marketplace.service.category.CategoryShop;
import com.marketplace.service.product.ShopProductInfo;

import java.util.List;

public interface ProductRepository {
    public boolean productExists(long productId);

    public long getCategoryId(long productId);

    public Product getProduct(long productId);

    public List<Shop> getShops();

    public List<ShopProduct> getShopProducts(long productId);

    public List<ProductCharacteristic> getProductCharacteristics(long productId);

    public List<ProductCharacteristic> getCategoryCharacteristics(long categoryId);

    public long addProduct(Product product);

    public boolean addShopProduct(ShopProductInfo info);

    public boolean addCharacteristicToProduct(long productId, long characteristicId);

    public boolean removeProduct(long productId);

    public boolean removeShopProduct(long productId, long shopId);
}
