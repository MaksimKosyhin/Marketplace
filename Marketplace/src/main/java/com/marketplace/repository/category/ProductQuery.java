package com.marketplace.repository.category;

import com.marketplace.service.category.SortingOption;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductQuery {
    private long categoryId;
    private List<Long> characteristicsId;
    private SortingOption sortingOption;
    private long startId;
    private long size;
    private boolean orderDescending;

    public ProductQuery(long categoryId, long size, List<Long> characteristicsId) {
        this.categoryId = categoryId;
        this.characteristicsId = characteristicsId;
        this.sortingOption = SortingOption.NO_SORTING;
        this.startId = 0;
        this.size = size;
    }

    public boolean isOrderDescending() {
        return orderDescending;
    }

    public SortingOption getSortingOption() {
        return sortingOption;
    }

    public int getNumberOfCharacteristics() {
        return characteristicsId.size();
    }
}
