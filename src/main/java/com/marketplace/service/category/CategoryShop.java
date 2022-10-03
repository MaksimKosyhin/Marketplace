package com.marketplace.service.category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CategoryShop {
    private long shopId;
    private String name;
    private String imgLocation;

    private boolean presentInCategory;
    public CategoryShop(String name, String imgLocation) {
        this.name = name;
        this.imgLocation = imgLocation;
    }
}
