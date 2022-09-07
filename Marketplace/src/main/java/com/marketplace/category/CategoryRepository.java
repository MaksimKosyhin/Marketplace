package com.marketplace.category;

import java.util.List;

public interface CategoryRepository {
    public boolean isParentCategory(long categoryId);
    public List<Category> getCategories(long parentId);
    public List<ProductInfo> getProducts(ProductQuery productQuery);
    public boolean removeCategory(long categoryId);
    public long addCategory(Category category);
    public long addCharacteristic(Characteristic characteristic);
    public List<Characteristic> getCharacteristics(long categoryId);
}
