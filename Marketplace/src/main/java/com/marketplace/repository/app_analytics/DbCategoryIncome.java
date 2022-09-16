package com.marketplace.repository.app_analytics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DbCategoryIncome {
    private long categoryId;
    private String name;
    private String imgLocation;
    private int income;
    private boolean removed;
}
