package com.marketplace.repository.category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Category {
    private Long categoryId;
    private String name;
    private Long parentId;
    private String imgLocation;

    public Category(String name, Long parentId, String imgLocation) {
        this.name = name;
        this.parentId = parentId;
        this.imgLocation = imgLocation;
    }
}
