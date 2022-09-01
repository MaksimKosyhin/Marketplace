package com.example.demo.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopProduct {
    private long productId;
    private String name;
    private String imgLocation;
    private long minPrice;
    private long maxPrice;
}
