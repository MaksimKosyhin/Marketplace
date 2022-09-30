package com.marketplace.service.product;

import com.marketplace.repository.product.ProductCharacteristic;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductCharacteristicMap {
    private String name;
    private Map<Long, String> values = new HashMap<>();
    private long includedCharacteristic = -1;

    public ProductCharacteristicMap(List<ProductCharacteristic> characteristics) {
        this.name = characteristics.get(0).getName();
        for (ProductCharacteristic characteristic: characteristics) {
            this.values.put(characteristic.getCharacteristicId(), characteristic.getCharacteristicValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicMap that = (ProductCharacteristicMap) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
