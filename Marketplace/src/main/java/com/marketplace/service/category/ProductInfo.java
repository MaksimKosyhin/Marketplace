package com.marketplace.service.category;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductInfo {
    private long productId;
    private String name;
    private FileSystemResource imgResource;
    private int minPrice;
    private int maxPrice;
    private int totalReviews;
}
