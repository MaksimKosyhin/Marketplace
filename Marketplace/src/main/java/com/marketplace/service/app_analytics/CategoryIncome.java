package com.marketplace.service.app_analytics;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryIncome {
    private long categoryId;
    private String name;
    private FileSystemResource imgResource;
    private int income;
    private boolean removed;
}
