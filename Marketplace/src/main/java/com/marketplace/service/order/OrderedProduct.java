package com.marketplace.service.order;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

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
    private FileSystemResource productImgResource;
    private int amount;
    private int price;
    private String shopName;
    private FileSystemResource shopImgResource;
}
