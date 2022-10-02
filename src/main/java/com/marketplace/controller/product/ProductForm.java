package com.marketplace.controller.product;

import com.marketplace.service.product.ProductCharacteristicMap;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductForm {
    private long categoryId;
    private String name;
    private MultipartFile imgFile;
    private List<ProductCharacteristicMap> characteristics;

    public ProductForm(long categoryId, List<ProductCharacteristicMap> characteristics) {
        this.categoryId = categoryId;
        this.characteristics = characteristics;
    }
}
