package com.marketplace.service.category;

import lombok.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Category {
    private long parentId;
    private long categoryId;
    private String name;
    private FileSystemResource imgResource;
    private MultipartFile imgFile;
    private boolean parentCategory;

    public Category(long parentId) {
        this.parentId = parentId;
        this.parentCategory = false;
    }
}
