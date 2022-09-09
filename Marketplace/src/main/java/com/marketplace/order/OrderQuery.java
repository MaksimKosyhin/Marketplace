package com.marketplace.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderQuery {
    private long orderId;
    private long productId;
    private long shopId;
    private long amount;
}
