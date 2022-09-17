package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.product.DbProduct;
import com.marketplace.repository.product.ShopProduct;
import com.marketplace.repository.product.ProductCharacteristic;
import com.marketplace.service.product.ProductInfo;
import com.marketplace.service.product.ProductDescription;
import com.marketplace.service.product.ShopProductDescription;
import com.marketplace.service.product.ShopProductInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ProductMapper {

    public ShopProduct toShopProduct(ShopProductInfo shopProductInfo) {
        ShopProduct shopProduct = new ShopProduct();

        shopProduct.setProductId(shopProductInfo.getProductId());
        shopProduct.setShopId(shopProductInfo.getShopId());
        shopProduct.setPrice(shopProductInfo.getPrice());
        shopProduct.setReviews(shopProductInfo.getReviews());
        shopProduct.setScore(shopProductInfo.getScore());

        return shopProduct;
    }

    public DbProduct toDbProduct(ProductInfo productInfo) {
        DbProduct dbProduct = new DbProduct();

        dbProduct.setName(dbProduct.getName());
        dbProduct.setCategoryId(dbProduct.getCategoryId());

        return dbProduct;
    }

    public ProductDescription toProductDescription(
            DbProduct dbProduct,
            List<ProductCharacteristic> characteristics) {

        ProductDescription productDescription = new ProductDescription();

        productDescription.setName(dbProduct.getName());
        productDescription.setProductId(dbProduct.getProductId());
        productDescription.setCategoryId(dbProduct.getCategoryId());
        productDescription.setCharacteristics(characteristics);

        return productDescription;
    }

    public ShopProductDescription toShopProductDescription(ShopProduct shopProduct) {
        ShopProductDescription shopProductDescription = new ShopProductDescription();

        shopProductDescription.setName(shopProduct.getName());
        shopProductDescription.setLink(shopProduct.getLink());
        shopProductDescription.setPrice(shopProduct.getPrice());
        shopProductDescription.setReviews(shopProduct.getReviews());
        shopProductDescription.setScore(shopProduct.getScore());

        return shopProductDescription;
    }
}
