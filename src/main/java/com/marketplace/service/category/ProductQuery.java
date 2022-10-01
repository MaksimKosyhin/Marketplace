package com.marketplace.service.category;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductQuery {

    private final boolean empty;

    private long categoryId;
    private List<CharacteristicMap> characteristics;
    private SortingOption sortingOption;
    private long size;
    private boolean orderDescending;

    public ProductQuery() {
        this.empty = true;
    }

    public ProductQuery(long categoryId, List<CharacteristicMap> characteristics, long size) {
        this.empty = false;

        this.categoryId = categoryId;
        this.characteristics = characteristics;
        this.sortingOption = SortingOption.NO_SORTING;
        this.size = size;
        this.orderDescending = false;
    }

    public int getNumberOfCharacteristics() {
        return characteristics
                .stream()
                .map(CharacteristicMap::getValues)
                .flatMap(Collection::stream)
                .filter(CharacteristicValue::isEnabled)
                .mapToInt(e -> 1)
                .sum();
    }

    public List<Long> getCharacteristicsId() {
        return characteristics
                .stream()
                .map(CharacteristicMap::getValues)
                .flatMap(Collection::stream)
                .filter(CharacteristicValue::isEnabled)
                .map(CharacteristicValue::getCharacteristicId)
                .collect(Collectors.toList());
    }
}
