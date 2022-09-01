package com.example.demo.category;

import java.util.List;

public interface CategoryRepository {
    public boolean isParentCategory(long categoryId);
    public List<String> getCategories(long parentId);
    public List<ShopProduct> getProducts(long categoryId);
    public void removeCategory(long categoryId);
    public void addCategory(String name, long parentId);
    public void addCharacteristic(long categoryId, String name, String value);
    public List<Characteristic> getCategoryCharacteristics(long categoryId);
}
