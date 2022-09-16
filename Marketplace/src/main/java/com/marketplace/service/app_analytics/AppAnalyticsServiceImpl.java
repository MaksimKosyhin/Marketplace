package com.marketplace.service.app_analytics;

import com.marketplace.repository.app_analytics.AppAnalyticsRepository;
import com.marketplace.repository.app_analytics.DbProductIncome;
import com.marketplace.repository.app_analytics.ProductIncomeQuery;
import com.marketplace.repository.app_analytics.Shop;
import com.marketplace.util.AppAnalyticsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppAnalyticsServiceImpl implements AppAnalyticsService {
    private final AppAnalyticsRepository repository;
    private final AppAnalyticsMapper mapper;

    public AppAnalyticsServiceImpl(AppAnalyticsRepository repository, AppAnalyticsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public int getTotalIncome(LocalDate from, LocalDate to) {
        return repository.getTotalIncome(from, to);
    }

    @Override
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to) {
        return repository.getCategoriesIncome(from, to)
                .stream()
                .map(mapper::toCategoryIncome)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductIncome> getProductsIncome(ProductIncomeQuery query) {
        List<DbProductIncome> incomes;

        if (query.getShopId() == 0) {
            incomes = repository.getProductsIncomeForAllShops(query);
        } else {
            incomes = repository.getProductsIncomeForShop(query);
        }

        return incomes
                .stream()
                .map(mapper::toProductIncome)
                .collect(Collectors.toList());
    }

    @Override
    public List<Shop> getShops(long categoryId) {
        return repository.getShops(categoryId);
    }

    @Override
    public int getNumberOfUsers(LocalDate from, LocalDate to) {
        return repository.getNumberOfUsers(from, to);
    }
}
