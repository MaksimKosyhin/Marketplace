package com.marketplace.service.category;

import com.marketplace.repository.category.DbCharacteristic;
import com.marketplace.repository.category.ProductQuery;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    public boolean isParentCategory(long categoryId);

    public long addCategory(Category category) throws IOException;

    public List<Category> getCategories(long parentId) throws Exception;

    public boolean removeCategory(long categoryId);

    public List<ProductInfo> getProducts(ProductQuery productQuery);

    public long addCharacteristic(DbCharacteristic dbCharacteristic);

    public List<Characteristic> getCharacteristics(long categoryId);
}
