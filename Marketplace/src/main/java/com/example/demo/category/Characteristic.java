package com.example.demo.category;

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
}
