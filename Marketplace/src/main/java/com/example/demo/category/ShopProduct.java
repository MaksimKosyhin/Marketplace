package com.example.demo.category;

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
    private long score;
    private long price;
    private long reviews;
}
