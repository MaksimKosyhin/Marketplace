package com.marketplace.repository.category;

import com.marketplace.service.category.SortingOption;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductQuery {
    private long categoryId;
    private long[] characteristics;
    private SortingOption sortingOption;
    private long startId;
    private long size;
    private boolean orderDescending;

    public boolean isOrderDescending() {
        return orderDescending;
    }

    public SortingOption getSortingOption() {
        return sortingOption;
    }

    public int getNumberOfCharacteristics() {
        return characteristics.length;
    }

    public Object[] getQueryParameters() {
        Object[] params = new Object[characteristics.length + 3];

        params[0] = categoryId;
        params[1] = startId;
        for(int i = 0; i < characteristics.length; i++) {
            params[i + 2] = characteristics[i];
        }
        params[params.length-1] = size;

        return  params;
    }
}
