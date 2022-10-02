package com.marketplace.repository.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Shop {
    private long shopId;
    private String name;
    private String imgLocation;

    private boolean selected = false;

    public Shop(String name, String imgLocation) {
        this.name = name;
        this.imgLocation = imgLocation;
    }
}
