package com.marketplace.repository.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ShopProduct {
    private long shopId;
    private long productId;
    private String name;
    private String link;
    private String imgLocation;
    private long score;
    private long price;
    private long reviews;

    public ShopProduct(long shopId, long productId, long score, long price, long reviews) {
        this.shopId = shopId;
        this.productId = productId;
        this.score = score;
        this.price = price;
        this.reviews = reviews;
    }
}
