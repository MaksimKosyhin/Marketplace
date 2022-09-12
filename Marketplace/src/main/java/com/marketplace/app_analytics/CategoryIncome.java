package com.marketplace.app_analytics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryIncome {
    private long categoryId;
    private String name;
    private String img_location;
    private int income;
    private boolean removed;
}
