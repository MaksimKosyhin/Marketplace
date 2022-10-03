package com.marketplace.service.product;

import com.marketplace.controller.product.ProductCharacteristicMap;
import com.marketplace.controller.product.ProductForm;
import com.marketplace.repository.product.Shop;

import java.util.List;

public interface ProductService {

    public long getCategoryId(long productId);

    public ProductDescription getProduct(long productId);

    public long addProduct(ProductForm productForm);

    public List<ProductCharacteristicMap> getCategoryCharacteristics(long categoryId);

    public List<Shop> getShops();

    public void removeProduct(long productId);

    public void addShopProduct(ShopProductInfo info);

    public void removeShopProduct(long productId, long shopId);
}
