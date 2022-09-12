package com.marketplace.repository.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderedProduct {
    private long orderId;
    private long shopId;
    private long productId;
    private String productName;
    private String productImgLocation;
    private int amount;
    private int price;
    private String shopName;
    private String shopImgLocation;
}
