package com.marketplace.repository.category;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DbCharacteristic {
    private Long characteristicId;
    private Long categoryId;
    private String name;
    private String characteristicValue;

    public DbCharacteristic(Long categoryId, String name, String characteristicValue) {
        this.categoryId = categoryId;
        this.name = name;
        this.characteristicValue = characteristicValue;
    }
}
