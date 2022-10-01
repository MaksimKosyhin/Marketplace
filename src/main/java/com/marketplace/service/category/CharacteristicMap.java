package com.marketplace.service.category;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CharacteristicMap {

    private String name;
    private List<CharacteristicValue> values;

    public CharacteristicMap(String name) {
        this.name = name;
        values = new ArrayList<>();
    }

    public CharacteristicMap(String name, List<CharacteristicValue> values) {
        this.name = name;
        this.values = values;
    }
}
