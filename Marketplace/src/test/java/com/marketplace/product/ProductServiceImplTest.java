package com.marketplace.product;

import com.marketplace.config.ImageLoader;
import com.marketplace.repository.product.DbProduct;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.service.product.ProductInfo;
import com.marketplace.service.product.ProductServiceImpl;
import com.marketplace.util.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private ImageLoader imageLoader;

    @InjectMocks
    @Autowired
    private ProductServiceImpl service;

    @Test
    public void returnsProducts() {
        //given
        long productId = 1;

        given(repository.productExists(productId)).willReturn(true);

        //when
        service.getProduct(productId);

        //then
        verify(mapper).toProductDescription(
                any(),
                any()
        );

        verify(repository).getProduct(productId);
        verify(repository).getProductCharacteristics(productId);
        verify(repository).getShopProducts(productId);
    }

    @Test
    public void addsProduct() {
        //given
        ProductInfo productinfo = new ProductInfo(
                1,
                "test",
                new byte[]{},
                new FileSystemResource(""),
                List.of(1L, 2L, 3L)
        );

        given(repository.addProduct(any())).willReturn(1L);
        given(mapper.toDbProduct(any(ProductInfo.class))).willReturn(new DbProduct());

        //when
        service.addProduct(productinfo);

        //then
        verify(repository).addProduct(any());
        verify(repository, times(3)).addCharacteristicToProduct(anyLong(), anyLong());
    }
}
