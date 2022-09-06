package com.marketplace.category;

public enum SortingOption {
    SCORE("average_score"),
    REVIEWS("total_reviews"),
    PRICE("min_price");

    private final String columnName;

    SortingOption(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
