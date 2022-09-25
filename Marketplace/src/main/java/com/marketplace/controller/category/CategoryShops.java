package com.marketplace.controller.category;

import com.marketplace.repository.category.Shop;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryShops {
    private long categoryId;
    private List<Shop> shops;
}
