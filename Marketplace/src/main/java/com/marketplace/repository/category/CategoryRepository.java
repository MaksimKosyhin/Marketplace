package com.marketplace.repository.category;

import com.marketplace.service.category.ProductQuery;

import java.util.List;

public interface CategoryRepository {
    public boolean categoryExists(long categoryId);

    public boolean isParentCategory(long categoryId);

    public boolean containsSubcategories(long categoryId);

    public List<Category> getCategories(long parentId);

    public List<ProductInfo> getProducts(ProductQuery productQuery);

    public boolean removeCategory(long categoryId);

    public long addCategory(Category category);

    public boolean addShopToCategory(long shopId, long categoryId);

    public List<Shop> getShops();

    public long addCharacteristic(Characteristic characteristic);

    public List<Characteristic> getCharacteristics(long categoryId);
    public List<Long> getCharacteristicsId(long categoryId);
}
