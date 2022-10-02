package com.marketplace.service.product;

import com.marketplace.repository.product.ProductCharacteristic;
import lombok.*;

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
    private Map<Long, String> options = new HashMap<>();
    private long includedCharacteristic = -1;

    public ProductCharacteristicMap(List<ProductCharacteristic> characteristics) {
        this.name = characteristics.get(0).getName();
        for (ProductCharacteristic characteristic: characteristics) {
            this.options.put(characteristic.getCharacteristicId(), characteristic.getCharacteristicValue());
        }
    }
}
