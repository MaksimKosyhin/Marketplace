package com.marketplace.repository.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductCharacteristic {
    private long characteristicId;
    private String name;
    private String characteristicValue;
}
