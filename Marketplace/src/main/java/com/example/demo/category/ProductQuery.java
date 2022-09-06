package com.example.demo.category;

import java.util.List;

public class ProductQuery {
    private long categoryId;
    private List<Long> characteristics;
    private String orderBy;
    long startId;
    long endId;

    public ProductQuery(long categoryId, List<Long> characteristics, String orderBy, long startId, long endId) {
        this.categoryId = categoryId;
        this.characteristics = characteristics;
        this.orderBy = orderBy;
        this.startId = startId;
        this.endId = endId;
    }

    public long getCategoryId() {
        return this.categoryId;
    }

//    public String getCharacteristics() {
//        String characteristics = this.characteristics.toString();
//        return  characteristics.substring(1, characteristics.length() - 1);
//    }

    public List<Long> getCharacteristics() {
        return this.characteristics;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public long getStartId() {
        return this.startId;
    }

    public long getEndId() {
        return this.endId;
    }
}
