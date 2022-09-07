package com.marketplace.category;

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
}
