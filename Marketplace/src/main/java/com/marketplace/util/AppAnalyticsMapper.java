package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.app_analytics.DbCategoryIncome;
import com.marketplace.repository.app_analytics.DbProductIncome;
import com.marketplace.service.app_analytics.CategoryIncome;
import com.marketplace.service.app_analytics.ProductIncome;

public class AppAnalyticsMapper {

    public ProductIncome toProductIncome(DbProductIncome dbIncome) {
        ProductIncome income = new ProductIncome();

        income.setProductId(dbIncome.getProductId());
        income.setName(dbIncome.getName());
        income.setNumberOfOrders(dbIncome.getNumberOfOrders());
        income.setIncome(dbIncome.getIncome());
        income.setRemoved(dbIncome.isRemoved());

        return income;
    }

    public CategoryIncome toCategoryIncome(DbCategoryIncome dbIncome) {
        CategoryIncome income = new CategoryIncome();

        income.setCategoryId(dbIncome.getCategoryId());
        income.setName(dbIncome.getName());
        income.setIncome(dbIncome.getIncome());
        income.setRemoved(dbIncome.isRemoved());

        return income;
    }
}
