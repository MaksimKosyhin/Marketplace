package com.marketplace.controller.category;

import com.marketplace.service.category.CategoryShop;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CategoryShopList {
    private long categoryId;
    private List<CategoryShop> values;
}
