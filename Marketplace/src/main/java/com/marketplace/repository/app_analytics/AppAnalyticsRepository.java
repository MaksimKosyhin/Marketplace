package com.marketplace.repository.app_analytics;

import java.time.LocalDate;
import java.util.List;

public interface AppAnalyticsRepository {
    public boolean categoryExists(long categoryId);
    public boolean isParentCategory(long categoryId);
    public int getTotalIncome(LocalDate from, LocalDate to);

    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to);

    public List<ProductIncome> getProductsIncomeForAllShops(ProductIncomeQuery query);

    public List<ProductIncome> getProductsIncomeForShop(ProductIncomeQuery query);

    public List<Shop> getShops(long categoryId);

    public int getNumberOfUsers(LocalDate from, LocalDate to);
}
