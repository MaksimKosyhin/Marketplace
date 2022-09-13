package com.marketplace.category;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.category.*;
import com.marketplace.service.category.*;
import com.marketplace.util.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private JdbcCategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    void checksIsParentCategory() {
        //when
        categoryService.isParentCategory(anyLong());
        //then
        verify(categoryRepository).isParentCategory(anyLong());
    }

    @Test
    void addsCategory() throws IOException {
        //given
        Category category = new Category(-1, "test", new byte[]{});

        given(categoryRepository.categoryExists(category.getParentId()))
                .willReturn(true);

        DbCategory dbCategory = new DbCategory(
                0L,
                category.getName(),
                category.getParentId(),
                "test"
        );

        given(categoryMapper.toDbCategory(category)).willReturn(dbCategory);

        //when
        categoryService.addCategory(category);

        //then
        verify(categoryRepository).addCategory(dbCategory);
    }

    @Test
    void returnsMappedCategories() throws Exception {
        //given
        long categoryId = 1;
        String imgLocation = "test";
        FileSystemResource resource = new FileSystemResource(imgLocation);

        DbCategory db1 = new DbCategory(2L, "test1", 1L, imgLocation);
        DbCategory db2 = new DbCategory(3L, "test2", 1L, imgLocation);

        Category c1 = new Category(db1.getCategoryId(), db1.getName(), resource);
        Category c2 = new Category(db2.getCategoryId(), db2.getName(), resource);

        given(categoryRepository.isParentCategory(categoryId))
                .willReturn(true);
        given(categoryMapper.toCategory(db1)).willReturn(c1);
        given(categoryMapper.toCategory(db2)).willReturn(c2);
        given(categoryRepository.getCategories(categoryId))
                .willReturn(List.of(db1, db2));

        //then
        assertThat(categoryService.getCategories(categoryId)).isEqualTo(List.of(c1, c2));
    }

    @Test
    void removeCategory() {
        //given
        given(categoryRepository.categoryExists(anyLong()))
                .willReturn(true);
        given(categoryRepository.removeCategory(anyLong()))
                .willReturn(true);

        //when
        categoryService.removeCategory(anyLong());

        //then
        verify(categoryRepository).removeCategory(anyLong());
    }

    @Test
    void returnsProductsInfo() {
        //given
        ProductQuery productQuery = new ProductQuery();

        given(categoryRepository.isParentCategory(productQuery.getCategoryId()))
                .willReturn(false);
        given(categoryRepository.categoryExists(productQuery.getCategoryId()))
                .willReturn(true);

        //when
        categoryService.getProducts(productQuery);

        //then
        verify(categoryRepository).getProducts(productQuery);
    }

    @Test
    void addsCharacteristic() {
        //given
        DbCharacteristic characteristic =
                new DbCharacteristic(-1L, 1L, "test", "test");

        given(categoryRepository.categoryExists(characteristic.getCategoryId()))
                .willReturn(true);

        //when
        categoryService.addCharacteristic(characteristic);

        //then
        verify(categoryRepository).addCharacteristic(characteristic);
    }

    @Test
    void groupsByName() {
        //given
        long categoryId = 1L;
        given(categoryRepository.categoryExists(categoryId)).willReturn(true);

        DbCharacteristic db1 =
                new DbCharacteristic(1L, categoryId, "name1", "val1");
        DbCharacteristic db2 =
                new DbCharacteristic(2L, categoryId, "name1", "val2");
        DbCharacteristic db3 =
                new DbCharacteristic(3L, categoryId, "name2", "val3");

        given(categoryRepository.getCharacteristics(categoryId)).willReturn(List.of(db1, db2, db3));

        Characteristic c1 = new Characteristic("name1", Map.of(1L, "val1", 2L, "val2"));
        Characteristic c2 = new Characteristic("name2", Map.of(3L, "val3"));

        //then
        assertThat(categoryService.getCharacteristics(categoryId)).isEqualTo(List.of(c2, c1));
    }
}