package com.marketplace.repository.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DbProduct {
    private long productId;
    private long categoryId;
    private String name;
    private String imgLocation;

    public DbProduct(long categoryId, String name, String imgLocation) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgLocation = imgLocation;
    }
}
