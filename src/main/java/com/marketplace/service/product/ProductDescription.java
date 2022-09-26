package com.marketplace.service.product;

import com.marketplace.repository.product.ProductCharacteristic;
import com.marketplace.repository.product.ShopProduct;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private String imgLocation;

    private List<ProductCharacteristic> characteristics;
    private List<ShopProduct> shops;
}
