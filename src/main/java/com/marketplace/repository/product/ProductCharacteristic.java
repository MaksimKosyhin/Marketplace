package com.marketplace.repository.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductCharacteristic {
    private String name;
    private String characteristicValue;
}
