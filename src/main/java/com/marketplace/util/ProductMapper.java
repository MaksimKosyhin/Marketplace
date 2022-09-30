package com.marketplace.util;

import com.marketplace.repository.product.Product;
import com.marketplace.service.product.ProductDescription;
import com.marketplace.controller.product.ProductInfo;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductInfo productInfo) {
        Product product = new Product();

        product.setName(product.getName());
        product.setCategoryId(product.getCategoryId());

        return product;
    }

    public ProductDescription toProductDescription(Product product) {

        ProductDescription productDescription = new ProductDescription();

        productDescription.setName(product.getName());
        productDescription.setProductId(product.getProductId());
        productDescription.setCategoryId(product.getCategoryId());

        return productDescription;
    }
}
