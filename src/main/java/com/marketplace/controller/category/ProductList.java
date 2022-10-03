package com.marketplace.controller.category;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
public class ProductList {
    private final boolean empty;

    private int currentPage;
    private Map<Integer, List<Long>> pages;
    private boolean firstPage;
    private boolean lastPage;

    public ProductList() {
        this.empty = true;
    }

    public ProductList(Map<Integer, List<Long>> pages) {
        this.empty = false;

        this.pages = pages;
        this.currentPage = 1;
        this.firstPage = true;
        this.lastPage = false;
    }

    public boolean setCurrentPage(int page) {
        if(page < 1 || page > pages.size()) {
            return false;
        } else {
            currentPage = page;

            if (currentPage == 0) {
                firstPage = true;
                lastPage = false;
            } else if(currentPage == pages.size()){
                firstPage = false;
                lastPage = true;
            } else {
                firstPage = false;
                lastPage = false;
            }

            return true;
        }
    }

    public List<Long> getProductsId() {
        return pages.get(currentPage);
    }
}
