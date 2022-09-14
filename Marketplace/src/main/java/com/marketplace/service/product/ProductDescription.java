package com.marketplace.service.product;

import com.marketplace.repository.product.ProductCharacteristic;
import lombok.*;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductDescription {
    private long productId;
    private long categoryId;
    private String name;
    private byte[] imgData;
    private FileSystemResource imgResource;

    private List<ProductCharacteristic> characteristics;
    private List<ShopProductDescription> shops;
}
