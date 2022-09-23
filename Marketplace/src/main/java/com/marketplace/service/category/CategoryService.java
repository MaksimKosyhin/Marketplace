package com.marketplace.service.category;

import com.marketplace.controller.category.CategoryShops;
import com.marketplace.repository.category.DbCharacteristic;
import com.marketplace.repository.category.ProductQuery;
import com.marketplace.repository.category.Shop;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    public boolean isParentCategory(long categoryId);
    public boolean containsSubcategories(long categoryId);

    public void addCategory(Category category);

    public List<Category> getCategories(long parentId);

    public void removeCategory(long categoryId);

    public void addShopsToCategory(CategoryShops shops);

    public List<Shop> getShops();

    public List<ProductInfo> getProducts(ProductQuery productQuery);

    public void addCharacteristic(DbCharacteristic dbCharacteristic);

    public List<Characteristic> getCharacteristics(long categoryId);
    public List<Long> getCharacteristicsId(long categoryId);
}
