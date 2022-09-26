package com.marketplace.service.app_analytics;

import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.config.exception.ParentCategoryException;
import com.marketplace.repository.app_analytics.*;

import java.time.LocalDate;
import java.util.List;

public class AppAnalyticsServiceImpl implements AppAnalyticsService {
    private final AppAnalyticsRepository repository;

    public AppAnalyticsServiceImpl(AppAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        return repository.isParentCategory(categoryId);
    }

    @Override
    public int getTotalIncome(LocalDate from, LocalDate to) {
        return repository.getTotalIncome(from, to);
    }

    @Override
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to) {
        return repository.getCategoriesIncome(from, to);
    }

    @Override
    public List<ProductIncome> getProductsIncome(ProductIncomeQuery query) {
        if(repository.isParentCategory(query.getCategoryId())) {
            throw new ParentCategoryException("Parent category doesn't contain products");
        } else if (!repository.categoryExists(query.getCategoryId())) {
            throw new NonExistingEntityException(
                    String.format("category with id: %d does not exist", query.getCategoryId()));
        }

        List<ProductIncome> incomes;

        if (query.getShopId() == 0) {
            incomes = repository.getProductsIncomeForAllShops(query);
        } else {
            incomes = repository.getProductsIncomeForShop(query);
        }

        return incomes;
    }


    @Override
    public List<Shop> getShops(long categoryId) {
        if (!repository.categoryExists(categoryId)) {
            throw new NonExistingEntityException(
                    String.format("category with id: %d does not exist", categoryId));
        } else {
            return repository.getShops(categoryId);
        }
    }

    @Override
    public int getNumberOfUsers(LocalDate from, LocalDate to) {
        return repository.getNumberOfUsers(from, to);
    }
}
