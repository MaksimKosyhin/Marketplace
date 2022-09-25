package com.marketplace.repository.category;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Characteristic {
    private Long characteristicId;
    private Long categoryId;
    private String name;
    private String characteristicValue;

    public Characteristic(Long categoryId, String name, String characteristicValue) {
        this.categoryId = categoryId;
        this.name = name;
        this.characteristicValue = characteristicValue;
    }
}
