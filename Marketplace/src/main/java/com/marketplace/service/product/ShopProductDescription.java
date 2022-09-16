package com.marketplace.service.product;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ShopProductDescription {
    private String name;
    private String link;
    private FileSystemResource resource;
    private long score;
    private long price;
    private long reviews;
}
