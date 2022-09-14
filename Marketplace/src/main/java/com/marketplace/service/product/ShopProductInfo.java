package com.marketplace.service.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ShopProductInfo {
    private long productId;
    private long shopId;
    private long score;
    private long price;
    private long reviews;
}
