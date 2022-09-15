package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.order.DbOrderedProduct;
import com.marketplace.service.order.OrderedProduct;

public class OrderMapper {
    private ImageLoader imageLoader;

    public OrderedProduct toOrderedProduct(DbOrderedProduct dbProduct) {
        OrderedProduct product = new OrderedProduct();

        product.setProductId(dbProduct.getProductId());
        product.setOrderId(dbProduct.getOrderId());
        product.setShopId(dbProduct.getShopId());
        product.setProductName(dbProduct.getProductName());
        product.setProductImgResource(
                imageLoader.findInFileSystem(dbProduct.getProductImgLocation()));
        product.setAmount(dbProduct.getAmount());
        product.setPrice(dbProduct.getPrice());
        product.setShopName(dbProduct.getShopName());
        product.setShopImgResource(
                imageLoader.findInFileSystem(dbProduct.getShopImgLocation()));

        return product;
    }
    

}
