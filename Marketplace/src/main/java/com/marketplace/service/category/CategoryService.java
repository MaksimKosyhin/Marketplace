package com.marketplace.service.category;

import com.marketplace.controller.category.CategoryInfo;
import com.marketplace.controller.category.CategoryShops;
import com.marketplace.repository.category.*;

import java.util.List;

public interface CategoryService {
    public boolean isParentCategory(long categoryId);
    public boolean containsSubcategories(long categoryId);

    public void addCategory(CategoryInfo categoryInfo);

    public List<Category> getCategories(long parentId);

    public void removeCategory(long categoryId);

    public void addShopsToCategory(CategoryShops shops);

    public List<Shop> getShops();

    public List<ProductInfo> getProducts(ProductQuery productQuery);

    public void addCharacteristic(Characteristic characteristic);

    public List<CharacteristicMap> getCharacteristics(long categoryId);
    public List<Long> getCharacteristicsId(long categoryId);
}
