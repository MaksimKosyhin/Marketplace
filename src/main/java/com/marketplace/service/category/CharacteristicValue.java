package com.marketplace.service.category;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CharacteristicValue {
    private long characteristicId;
    private String value;
    private boolean enabled = false;

    public CharacteristicValue(long characteristicId, String value) {
        this.characteristicId = characteristicId;
        this.value = value;
    }
}
