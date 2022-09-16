package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.app_analytics.DbCategoryIncome;
import com.marketplace.repository.app_analytics.DbProductIncome;
import com.marketplace.service.app_analytics.CategoryIncome;
import com.marketplace.service.app_analytics.ProductIncome;

public class AppAnalyticsMapper {

    private final ImageLoader loader;

    public AppAnalyticsMapper(ImageLoader loader) {
        this.loader = loader;
    }

    public ProductIncome toProductIncome(DbProductIncome dbIncome) {
        return new ProductIncome(
                dbIncome.getProductId(),
                dbIncome.getName(),
                loader.findInFileSystem(dbIncome.getImgLocation()),
                dbIncome.getNumberOfOrders(),
                dbIncome.getIncome(),
                dbIncome.isRemoved()
        );
    }

    public CategoryIncome toCategoryIncome(DbCategoryIncome dbIncome) {
        return new CategoryIncome(
                dbIncome.getCategoryId(),
                dbIncome.getName(),
                loader.findInFileSystem(dbIncome.getImgLocation()),
                dbIncome.getIncome(),
                dbIncome.isRemoved()
        );
    }
}
