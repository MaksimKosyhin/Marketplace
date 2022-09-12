package com.marketplace.repository.app_analytics;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductIncomeQuery {
    private long categoryId;
    private LocalDate from;
    private LocalDate to;
}
