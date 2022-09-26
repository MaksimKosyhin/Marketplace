package com.marketplace.service.category;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.controller.category.CategoryInfo;
import com.marketplace.controller.category.CategoryShops;
import com.marketplace.repository.category.ProductInfo;
import com.marketplace.repository.category.*;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.config.exception.ParentCategoryException;
import com.marketplace.util.CategoryMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final ImageLoader imageLoader;

    public CategoryServiceImpl(CategoryRepository repository,
                               CategoryMapper mapper,
                               ImageLoader imageLoader) {

        this.repository = repository;
        this.mapper = mapper;
        this.imageLoader = imageLoader;
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        return repository.isParentCategory(categoryId);
    }

    @Override
    public void addCategory(CategoryInfo info) {
        if (!repository.categoryExists(info.getParentId())) {
            throw new NonExistingEntityException(
                    String.format(
                            "parent category with id: %d does not exist", info.getParentId()));
        }

        Category category = mapper.toCategory(info);

        String imgLocation = imageLoader.save(
                info.getImgFile(),
                "category"
        );

        category.setImgLocation(imgLocation);

        if (repository.addCategory(category) == -1) {
            throw new AddEntryException("category was not added");
        }
    }

    @Override
    public List<Category> getCategories(long parentId) {
        if (!repository.isParentCategory(parentId)) {
            throw new ParentCategoryException(
                    "This category doesn't have subcategories"
            );
        }

        return repository.getCategories(parentId);
    }

    @Override
    public void removeCategory(long categoryId) {
        if (!repository.categoryExists(categoryId)) {
            throw new NonExistingEntityException(
                    String.format("Category with id: %d doesn't exist", categoryId)
            );
        } else if (repository.containsSubcategories(categoryId)) {
            throw new ParentCategoryException(
                    "Cannot remove category which has child categories"
            );
        } else if (!repository.removeCategory(categoryId)) {
            throw new ModifyingEntryException(
                    String.format("category with id: %d was not removed", categoryId));
        }
    }

    @Override
    @Transactional
    public void addShopsToCategory(CategoryShops shops) {
        if (isParentCategory(shops.getCategoryId())) {
            throw new AddEntryException("Cannot add shops to parent category");
        }

        for(Shop shop: shops.getShops()) {
            if(!repository.addShopToCategory(shop.getShopId(), shops.getCategoryId())) {
                throw new AddEntryException("shop was not added to category");
            }
        }
    }

    @Override
    public List<Shop> getShops() {
        return repository.getShops();
    }

    @Override
    public List<ProductInfo> getProducts(ProductQuery productQuery) {
        if (repository.isParentCategory(productQuery.getCategoryId())) {
            throw new ParentCategoryException("This category includes other categories");
        } else if (!repository.categoryExists(productQuery.getCategoryId())) {
            throw new NonExistingEntityException("Category does not exist");
        } else {
            return repository.getProducts(productQuery);
        }
    }

    @Override
    public void addCharacteristic(Characteristic characteristic) {
        if (repository.categoryExists(characteristic.getCategoryId())) {
            if (repository.addCharacteristic(characteristic) == -1) {
                throw new ModifyingEntryException("Characteristic was not added");
            }
        } else {
            throw new NonExistingEntityException(
                    "category for this characteristic does not exist");
        }
    }

    @Override
    public List<CharacteristicMap> getCharacteristics(long categoryId) {
        if (repository.categoryExists(categoryId)) {
            return combineCharacteristicValues(getGroupedByName(categoryId));
        } else {
            throw new NonExistingEntityException("Category does not exist");
        }
    }

    @Override
    public List<Long> getCharacteristicsId(long categoryId) {
        if(repository.categoryExists(categoryId)) {
            return repository.getCharacteristicsId(categoryId);
        } else {
            throw new NonExistingEntityException(
                    String.format("category with id: %s does not exist", categoryId));
        }
    }

    private List<CharacteristicMap> combineCharacteristicValues(Map<String, Set<Characteristic>> map) {
        return map.entrySet()
                .stream()
                .map(this::combineCharacteristicValues)
                .collect(Collectors.toList());
    }

    private CharacteristicMap combineCharacteristicValues(Map.Entry<String, Set<Characteristic>> entry) {
        CharacteristicMap result = new CharacteristicMap(entry.getKey());

        for (Characteristic c : entry.getValue()) {
            result.getValues().put(
                    c.getCharacteristicId(),
                    c.getCharacteristicValue()
            );
        }

        return result;
    }

    private Map<String, Set<Characteristic>> getGroupedByName(long categoryId) {
        return repository.getCharacteristics(categoryId)
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Characteristic::getName,
                                Collectors.toSet()
                        )
                );
    }
}
