package com.marketplace.category;

import com.marketplace.config.ImageLoader;
import com.marketplace.controller.category.CategoryInfo;
import com.marketplace.service.category.CharacteristicMap;
import com.marketplace.repository.category.*;
import com.marketplace.service.category.*;
import com.marketplace.util.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private JdbcCategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ImageLoader imageLoader;

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
        CategoryInfo info = new CategoryInfo();

        given(categoryRepository.categoryExists(info.getParentId()))
                .willReturn(true);

        Category category = new Category(
                0L,
                info.getName(),
                info.getParentId(),
                "test",
                true
        );

        given(categoryMapper.toCategory(info)).willReturn(category);

        //when
        categoryService.addCategory(info);

        //then
        verify(categoryRepository).addCategory(category);
    }

    @Test
    void returnsMappedCategories() throws Exception {
        //given
        long categoryId = 1;
        String imgLocation = "test";

        Category db1 = new Category(2L, "test1", 1L, imgLocation, true);
        Category db2 = new Category(3L, "test2", 1L, imgLocation, true);

        CategoryInfo c1 = new CategoryInfo();
        CategoryInfo c2 = new CategoryInfo();

        given(categoryRepository.isParentCategory(categoryId))
                .willReturn(true);
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
        Characteristic characteristic =
                new Characteristic(-1L, 1L, "test", "test");

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

        Characteristic db1 =
                new Characteristic(1L, categoryId, "name1", "val1");
        Characteristic db2 =
                new Characteristic(2L, categoryId, "name1", "val2");
        Characteristic db3 =
                new Characteristic(3L, categoryId, "name2", "val3");

        given(categoryRepository.getCharacteristics(categoryId)).willReturn(List.of(db1, db2, db3));

        CharacteristicMap c1 = new CharacteristicMap("name1", Map.of(1L, "val1", 2L, "val2"));
        CharacteristicMap c2 = new CharacteristicMap("name2", Map.of(3L, "val3"));

        //then
        assertThat(categoryService.getCharacteristics(categoryId)).isEqualTo(List.of(c2, c1));
    }
}