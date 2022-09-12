package com.marketplace.repository.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
    private long product_id;
    private long category_id;
    private String name;
    private String imgLocation;

    public Product(long category_id, String name, String imgLocation) {
        this.category_id = category_id;
        this.name = name;
        this.imgLocation = imgLocation;
    }
}
