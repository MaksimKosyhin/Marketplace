package com.marketplace.service.product;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductInfo {
    private long categoryId;
    private String name;
    private byte[] imgData;
    private FileSystemResource imgResource;

    private List<Long> characteristics;
}
