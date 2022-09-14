package com.marketplace.service.category;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.repository.category.CategoryRepository;
import com.marketplace.repository.category.DbCategory;
import com.marketplace.repository.category.DbCharacteristic;
import com.marketplace.repository.category.ProductQuery;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.config.exception.ParentCategoryException;
import com.marketplace.util.CategoryMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        return categoryRepository.isParentCategory(categoryId);
    }

    @Override
    public void addCategory(Category category) throws IOException {
        if (!categoryRepository.categoryExists(category.getParentId())) {
            throw new NonExistingEntityException(
                    "parent category for this category does not exist");
        }

        DbCategory dbCategory = categoryMapper.toDbCategory(category);

        if (categoryRepository.addCategory(dbCategory) == -1) {
            throw new AddEntryException("category was not added");
        }
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
                .map(categoryMapper::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCategory(long categoryId) {
        if (!categoryRepository.categoryExists(categoryId)) {
            throw new NonExistingEntityException(
                    String.format("Category with id: %d doesn't exist", categoryId)
            );
        } else if (!categoryRepository.removeCategory(categoryId)) {
            throw new ModifyingEntryException(
                    String.format("Category with id: %d was not modified", categoryId)
            );
        }
    }

    @Override
    public List<ProductInfo> getProducts(ProductQuery productQuery) {
        if (categoryRepository.isParentCategory(productQuery.getCategoryId())) {
            throw new ParentCategoryException("This category includes other categories");
        } else if (!categoryRepository.categoryExists(productQuery.getCategoryId())) {
            throw new NonExistingEntityException("Category does not exist");
        } else {
            categoryMapper.toProductInfo(null);

            return categoryRepository.getProducts(productQuery)
                    .stream()
                    .map(categoryMapper::toProductInfo)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void addCharacteristic(DbCharacteristic dbCharacteristic) {
        if (categoryRepository.categoryExists(dbCharacteristic.getCategoryId())) {
            if (categoryRepository.addCharacteristic(dbCharacteristic) == -1) {
                throw new ModifyingEntryException("Characteristic was not added");
            }
        } else {
            throw new NonExistingEntityException(
                    "category for this characteristic does not exist");
        }
    }

    @Override
    public List<Characteristic> getCharacteristics(long categoryId) {
        if (categoryRepository.categoryExists(categoryId)) {
            return combineCharacteristicValues(getGroupedByName(categoryId));
        } else {
            throw new NonExistingEntityException("Category does not exist");
        }
    }

    private List<Characteristic> combineCharacteristicValues(Map<String, Set<DbCharacteristic>> map) {
        return map.entrySet()
                .stream()
                .map(this::combineCharacteristicValues)
                .collect(Collectors.toList());
    }

    private Characteristic combineCharacteristicValues(Map.Entry<String, Set<DbCharacteristic>> entry) {
        Characteristic result = new Characteristic(entry.getKey());

        for (DbCharacteristic c : entry.getValue()) {
            result.getValues().put(
                    c.getCharacteristicId(),
                    c.getCharacteristicValue()
            );
        }

        return result;
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
}
