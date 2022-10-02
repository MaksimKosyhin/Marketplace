package com.marketplace.service.product;

import com.marketplace.controller.product.ProductInfo;
import com.marketplace.repository.product.Shop;
import com.marketplace.service.category.CategoryShop;

import java.util.List;

public interface ProductService {

    public long getCategoryId(long productId);

    public ProductDescription getProduct(long productId);

    public long addProduct(ProductInfo productInfo);

    public List<ProductCharacteristicMap> getCategoryCharacteristic(long categoryId);

    public List<Shop> getShops();

    public void removeProduct(long productId);

    public void addShopProduct(ShopProductInfo info);

    public void removeShopProduct(long productId, long shopId);
}
