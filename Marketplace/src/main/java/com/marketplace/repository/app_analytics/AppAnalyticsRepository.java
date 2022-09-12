package com.marketplace.repository.app_analytics;

import java.time.LocalDate;
import java.util.List;

public interface AppAnalyticsRepository {
    public int getTotalIncome(LocalDate from, LocalDate to);
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to);
    public List<ProductIncome> getProductsIncome(ProductIncomeQuery productIncomeQuery);
    public List<Shop> getShops(long categoryId);
    public int getNumberOfUsers(LocalDate from, LocalDate to);
}
