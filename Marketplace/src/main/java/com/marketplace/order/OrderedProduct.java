package com.marketplace.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderedProduct {
    private long orderId;
    private String productName;
    private String productImgLocation;
    private int amount;
    private int price;
    private String shopName;
    private String shopImgLocation;
}
