package com.marketplace.service.app_analytics;

import com.marketplace.repository.app_analytics.ProductIncomeQuery;
import com.marketplace.repository.app_analytics.Shop;

import java.time.LocalDate;
import java.util.List;

public interface AppAnalyticsService {
    public int getTotalIncome(LocalDate from, LocalDate to);

    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to);

    public List<ProductIncome> getProductsIncome(ProductIncomeQuery query);

    public List<Shop> getShops(long categoryId);

    public int getNumberOfUsers(LocalDate from, LocalDate to);
}
