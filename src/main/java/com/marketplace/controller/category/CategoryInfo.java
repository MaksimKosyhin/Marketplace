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
    private String name;
    private MultipartFile imgFile;
    private boolean parentCategory;
}
