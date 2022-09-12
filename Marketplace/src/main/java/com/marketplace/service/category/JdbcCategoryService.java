package com.marketplace.service.category;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.category.CategoryRepository;
import com.marketplace.repository.category.DbCategory;
import com.marketplace.repository.category.DbCharacteristic;
import com.marketplace.repository.category.ProductQuery;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.config.exception.ParentCategoryException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageLoader imageLoader;

    public JdbcCategoryService(CategoryRepository categoryRepository, ImageLoader imageLoader) {
        this.categoryRepository = categoryRepository;
        this.imageLoader = imageLoader;
    }

    private Map<String, Set<DbCharacteristic>> getGroupedByName(long categoryId) {
        return categoryRepository.getCharacteristics(categoryId)
                .stream()
                .collect(
                        Collectors.groupingBy(
                                DbCharacteristic::getName,
                                Collectors.toSet()
                        )
                );
    }

    private Characteristic combineCharacteristicValues(Set<DbCharacteristic> set) {
        Characteristic result = new Characteristic();
        for (DbCharacteristic c : set) {
            result.getValues().put(
                    c.getCharacteristicId(),
                    c.getCharacteristicValue()
            );
        }
        return result;
    }

    private List<Characteristic> combineCharacteristicValues(Map<String, Set<DbCharacteristic>> map) {
        return map.entrySet()
                .stream()
                .map(entry -> {
                    return combineCharacteristicValues(entry.getValue());
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        return categoryRepository.isParentCategory(categoryId);
    }

    @Override
    public long addCategory(Category category) throws IOException {

        if (!categoryRepository.categoryExists(category.getParentId())) {
            throw new NonExistingEntityException(
                    "parent category for this category does not exist");
        }

        String img_location = imageLoader.save(
                category.getImgData(),
                Paths.get("category", category.getName())
        );

        DbCategory dbCategory = new DbCategory(
                category.getCategoryId(),
                category.getName(),
                category.getParentId(),
                img_location
        );

        return categoryRepository.addCategory(dbCategory);
    }

    @Override
    public List<Category> getCategories(long parentId) throws Exception {
        if (!categoryRepository.isParentCategory(parentId)) {
            throw new ParentCategoryException(
                    "This category doesn't have subcategories"
            );
        }

        return categoryRepository.getCategories(parentId)
                .stream()
                .map(dbCategory -> new Category(
                        dbCategory.getCategoryId(),
                        dbCategory.getName(),
                        imageLoader.findInFileSystem(
                                dbCategory.getImgLocation()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeCategory(long categoryId) {
        return categoryRepository.removeCategory(categoryId);
    }

    @Override
    public List<ProductInfo> getProducts(ProductQuery productQuery) {
        if (categoryRepository.isParentCategory(productQuery.getCategoryId())) {
            throw new ParentCategoryException("This category includes other categories");
        }

        return categoryRepository.getProducts(productQuery)
                .stream()
                .map(dbProductInfo -> new ProductInfo(
                        dbProductInfo.getProductId(),
                        dbProductInfo.getName(),
                        imageLoader.findInFileSystem(dbProductInfo.getImgLocation()),
                        dbProductInfo.getMinPrice(),
                        dbProductInfo.getMaxPrice(),
                        dbProductInfo.getTotalReviews())
                )
                .collect(Collectors.toList());
    }

    @Override
    public long addCharacteristic(DbCharacteristic dbCharacteristic) {
        if (categoryRepository.categoryExists(dbCharacteristic.getCategoryId())) {
            return categoryRepository.addCharacteristic(dbCharacteristic);
        } else {
            throw new NonExistingEntityException(
                    "category for this characteristic does not exist");
        }
    }

    @Override
    public List<Characteristic> getCharacteristics(long categoryId) {
        return combineCharacteristicValues(
                getGroupedByName(categoryId)
        );
    }
}
