package com.marketplace.controller.category;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryInfo {
    private long parentId;
    private String parentName;
    private String name;
    private MultipartFile imgFile;
    private boolean parentCategory;

    public CategoryInfo(long parentId, String parentName) {
        this.parentId = parentId;
        this.parentCategory = false;
    }
}
