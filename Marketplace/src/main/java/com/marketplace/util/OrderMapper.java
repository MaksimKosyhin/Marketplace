package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.order.DbOrderedProduct;
import com.marketplace.service.order.OrderedProduct;

public class OrderMapper {

    public OrderedProduct toOrderedProduct(DbOrderedProduct dbProduct) {
        OrderedProduct product = new OrderedProduct();

        product.setProductId(dbProduct.getProductId());
        product.setOrderId(dbProduct.getOrderId());
        product.setShopId(dbProduct.getShopId());
        product.setProductName(dbProduct.getProductName());
        product.setAmount(dbProduct.getAmount());
        product.setPrice(dbProduct.getPrice());
        product.setShopName(dbProduct.getShopName());

        return product;
    }
    

}
