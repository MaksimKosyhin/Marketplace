package com.marketplace.util;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.category.DbCategory;
import com.marketplace.repository.category.DbProductInfo;
import com.marketplace.service.category.Category;
import com.marketplace.service.category.ProductInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CategoryMapper {
    private final ImageLoader imageLoader;

    public CategoryMapper(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public Category toCategory(DbCategory dbCategory) {
        Category category = new Category();

        category.setCategoryId(dbCategory.getCategoryId());
        category.setName(dbCategory.getName());
        category.setImgResource(
                imageLoader.findInFileSystem(
                        dbCategory.getImgLocation()));

        return category;
    }

    public DbCategory toDbCategory(Category category) throws IOException {
        DbCategory dbCategory = new DbCategory();

        dbCategory.setName(category.getName());
        dbCategory.setParentId(category.getParentId());

        String imgLocation = imageLoader.save(
                category.getImgData(),
                "category",
                category.getName()
        );
        dbCategory.setImgLocation(imgLocation);

        return dbCategory;
    }

    public ProductInfo toProductInfo(DbProductInfo dbProductInfo) {
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId(dbProductInfo.getProductId());
        productInfo.setName(dbProductInfo.getName());
        productInfo.setImgResource(imageLoader.findInFileSystem(dbProductInfo.getImgLocation()));
        productInfo.setMinPrice(dbProductInfo.getMinPrice());
        productInfo.setMaxPrice(productInfo.getMaxPrice());
        productInfo.setTotalReviews(dbProductInfo.getTotalReviews());

        return productInfo;
    }
}
