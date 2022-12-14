package com.marketplace.repository.category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductInfo {
    private long productId;
    private String name;
    private String imgLocation;
    private int minPrice;
    private int maxPrice;
    private int totalReviews;
}
