package com.marketplace.service.app_analytics;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.app_analytics.*;
import com.marketplace.util.AppAnalyticsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppAnalyticsServiceImpl implements AppAnalyticsService {
    private final AppAnalyticsRepository repository;
    private final AppAnalyticsMapper mapper;
    private final ImageLoader loader;

    public AppAnalyticsServiceImpl(AppAnalyticsRepository repository, AppAnalyticsMapper mapper, ImageLoader loader) {
        this.repository = repository;
        this.mapper = mapper;
        this.loader = loader;
    }

    @Override
    public int getTotalIncome(LocalDate from, LocalDate to) {
        return repository.getTotalIncome(from, to);
    }

    @Override
    public List<CategoryIncome> getCategoriesIncome(LocalDate from, LocalDate to) {
        return repository.getCategoriesIncome(from, to)
                .stream()
                .map(this::toCategoryIncome)
                .collect(Collectors.toList());
    }

    private CategoryIncome toCategoryIncome(DbCategoryIncome dbIncome) {
        CategoryIncome income = mapper.toCategoryIncome(dbIncome);
        income.setImgResource(loader.toFileSystemResource(dbIncome.getImgLocation()));
        return income;
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
                .map(this::toProductIncome)
                .collect(Collectors.toList());
    }

    private ProductIncome toProductIncome(DbProductIncome dbIncome) {
        ProductIncome income = mapper.toProductIncome(dbIncome);
        income.setImgResource(
                loader.toFileSystemResource(dbIncome.getImgLocation()));
        return income;
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
