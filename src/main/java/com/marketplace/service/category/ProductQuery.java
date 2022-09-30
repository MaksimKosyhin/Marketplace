package com.marketplace.service.category;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductQuery {
    private long categoryId;
    private List<Long> characteristicsId;
    private SortingOption sortingOption;
    private long size;
    private boolean orderDescending;

    public ProductQuery(long categoryId, List<Long> characteristicsId, long size) {
        this.categoryId = categoryId;
        this.characteristicsId = characteristicsId;
        this.sortingOption = SortingOption.NO_SORTING;
        this.size = size;
        this.orderDescending = false;
    }

    public ProductQuery() {
        this.sortingOption = SortingOption.NO_SORTING;
    }

    public int getNumberOfCharacteristics() {
        return characteristicsId.size();
    }
}
