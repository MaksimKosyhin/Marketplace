package com.marketplace.service.category;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Category {
    private long parentId;
    private long categoryId;
    private String name;
    private byte[] imgData;
    private FileSystemResource imgResource;

    public Category(long parentId, String name, byte[] imgData) {
        this.parentId = parentId;
        this.name = name;
        this.imgData = imgData;
    }

    public Category(long categoryId, String name, FileSystemResource imgResource) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgResource = imgResource;
    }
}
