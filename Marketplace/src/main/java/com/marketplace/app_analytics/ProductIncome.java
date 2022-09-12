package com.marketplace.app_analytics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductIncome {
    private long product_id;
    private String name;
    private String imgLocation;
    private int numberOfOrders;
    private int income;
    private boolean removed;
}
