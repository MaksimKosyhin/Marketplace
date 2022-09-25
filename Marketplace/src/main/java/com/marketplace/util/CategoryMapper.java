package com.marketplace.util;

import com.marketplace.controller.category.CategoryInfo;
import com.marketplace.repository.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryInfo info) {
        Category category = new Category();

        category.setName(info.getName());
        category.setParentId(info.getParentId());
        category.setParentCategory(info.isParentCategory());

        return category;
    }
}
