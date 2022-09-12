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
        Object[] parameters = new Object[characteristics.length + 3];
        for(int i = 0; i < characteristics.length; i++) {
            parameters[i] = characteristics[i];
        }
        parameters[characteristics.length] = categoryId;
        parameters[characteristics.length + 1] = startId;
        parameters[characteristics.length + 2] = size;

        return  parameters;
    }
}
