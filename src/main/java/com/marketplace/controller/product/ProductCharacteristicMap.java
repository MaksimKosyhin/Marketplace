package com.marketplace.controller.product;

import com.marketplace.controller.product.CharacteristicOption;
import com.marketplace.repository.product.ProductCharacteristic;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ProductCharacteristicMap {
    private String name;
    private List<CharacteristicOption> options = new ArrayList<>();
    private long includedCharacteristic = -1;

    public ProductCharacteristicMap(List<ProductCharacteristic> characteristics) {
        this.name = characteristics.get(0).getName();
        for (ProductCharacteristic characteristic: characteristics) {
            this.options.add(new CharacteristicOption(characteristic.getCharacteristicId(), characteristic.getCharacteristicValue()));
        }
    }
}
