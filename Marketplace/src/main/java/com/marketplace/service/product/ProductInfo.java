package com.marketplace.service.product;

import lombok.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile imgFile;
    private FileSystemResource imgResource;

    private List<Long> characteristics;
}
