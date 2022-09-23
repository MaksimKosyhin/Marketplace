package com.marketplace.util;

import com.marketplace.repository.category.DbCategory;
import com.marketplace.repository.category.DbProductInfo;
import com.marketplace.service.category.Category;
import com.marketplace.service.category.ProductInfo;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(DbCategory dbCategory) {
        Category category = new Category();

        category.setCategoryId(dbCategory.getCategoryId());
        category.setName(dbCategory.getName());
        category.setParentCategory(dbCategory.isParentCategory());

        return category;
    }

    public DbCategory toDbCategory(Category category) {
        DbCategory dbCategory = new DbCategory();

        dbCategory.setName(category.getName());
        dbCategory.setParentId(category.getParentId());
        dbCategory.setParentCategory(category.isParentCategory());

        return dbCategory;
    }

    public ProductInfo toProductInfo(DbProductInfo dbProductInfo) {
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId(dbProductInfo.getProductId());
        productInfo.setName(dbProductInfo.getName());
        productInfo.setMinPrice(dbProductInfo.getMinPrice());
        productInfo.setMaxPrice(productInfo.getMaxPrice());
        productInfo.setTotalReviews(dbProductInfo.getTotalReviews());

        return productInfo;
    }
}
