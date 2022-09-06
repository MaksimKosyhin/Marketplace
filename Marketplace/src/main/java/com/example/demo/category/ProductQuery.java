package com.example.demo.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductQuery {
    private long categoryId;
    private List<Long> characteristics;
    private OrderBy orderBy;
    long startId;
    long endId;

    public ProductQuery(long categoryId, List<Long> characteristics, OrderBy orderBy, long startId, long endId) {
        this.categoryId = categoryId;
        this.characteristics = characteristics;
        this.orderBy = orderBy;
        this.startId = startId;
        this.endId = endId;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public String getCharacteristicsInsertParametersTemplate() {
        return String.join(",", Collections.nCopies(characteristics.size(), "?"));
    }

    public Object[] getQueryParameters() {
        List<Object> parameters = new ArrayList<>(characteristics);
        parameters.add(categoryId);
        parameters.add(startId);
        parameters.add(endId);

        return  parameters.toArray();
    }
}
