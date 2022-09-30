package com.marketplace.controller.shop;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ShopInfo {
    private String name;
    private MultipartFile imgFile;
}
