package com.marketplace.service.category;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Characteristic {

    private String name;
    private Map<Long, String> values;

    public Characteristic(String name) {
        this.name = name;
        values = new HashMap<>();
    }

    public Characteristic(String name, Map<Long, String> values) {
        this.name = name;
        this.values = new HashMap<>(values);
    }
}
