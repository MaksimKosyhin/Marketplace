package com.marketplace.repository.category;

import java.util.List;

public interface CategoryRepository {
    public boolean categoryExists(long categoryId);

    public boolean isParentCategory(long categoryId);

    public List<DbCategory> getCategories(long parentId);

    public List<DbProductInfo> getProducts(ProductQuery productQuery);

    public boolean removeCategory(long categoryId);

    public long addCategory(DbCategory dbCategory);

    public long addCharacteristic(DbCharacteristic dbCharacteristic);

    public List<DbCharacteristic> getCharacteristics(long categoryId);
}
