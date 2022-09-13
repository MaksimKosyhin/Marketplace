package com.marketplace.repository.category;

import com.marketplace.service.category.SortingOption;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ColumnConverter {
    private final Map<SortingOption, String> converter = Map.of(
            SortingOption.REVIEWS, "total_reviews",
            SortingOption.PRICE, "min_price"
    );

    public String getColumnName(SortingOption sortingOption) {
        return converter.get(sortingOption);
    }

}
