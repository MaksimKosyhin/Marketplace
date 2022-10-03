package com.marketplace.service.category;

import com.marketplace.controller.category.CategoryInfo;
import com.marketplace.controller.category.CategoryShopList;
import com.marketplace.controller.category.ProductList;
import com.marketplace.repository.category.Category;
import com.marketplace.repository.category.Characteristic;
import com.marketplace.repository.category.ProductInfo;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    public boolean isParentCategory(long categoryId);

    public void addCategory(CategoryInfo categoryInfo);

    public List<Category> getCategories(long parentId);

    public void removeCategory(long categoryId);

    public void addShopsToCategory(CategoryShopList shops);

    public Map<Boolean, List<CategoryShop>> getShops(long categoryId);

    public List<ProductInfo> getProducts(List<Long> productsId);

    public ProductList getProductList(ProductQuery query);

    public void addCharacteristic(Characteristic characteristic);

    public List<CharacteristicMap> getCharacteristics(long categoryId);
}
