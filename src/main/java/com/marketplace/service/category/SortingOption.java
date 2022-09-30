package com.marketplace.service.category;

public enum SortingOption {
    NO_SORTING("no sorting"),
    REVIEWS("reviews"),
    PRICE("price");

    public final String name;

    SortingOption(String name) {
        this.name = name;
    }
}
