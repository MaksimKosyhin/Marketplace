package com.marketplace.category;

import java.util.List;

public interface CategoryRepository {
    public boolean isParentCategory(long categoryId);
    public List<Category> getCategories(long parentId);
    public List<ProductInfo> getProducts(ProductQuery productQuery);
    public void removeCategory(long categoryId);
    public void addCategory(Category category);
    public void addCharacteristic(Characteristic characteristic);
    public List<Characteristic> getCharacteristics(long categoryId);
}
