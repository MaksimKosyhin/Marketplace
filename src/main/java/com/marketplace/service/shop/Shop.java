package com.marketplace.service.shop;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Shop {
    private long shopId;
    private String name;
    private String imgLocation;
}
