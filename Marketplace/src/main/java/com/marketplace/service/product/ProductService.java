package com.marketplace.service.product;

import com.marketplace.repository.product.Shop;

import java.io.IOException;

public interface ProductService {
    public ProductDescription getProduct(long productId);

    public void addProduct(ProductInfo productInfo) throws IOException;

    public void removeProduct(long productId);

    public void addShopProduct(ShopProductInfo shopProductInfo);

    public void removeShopProduct(long productId, long shopId);

    public void addShop(Shop shop);

    public void removeShop(long shopId);
}
