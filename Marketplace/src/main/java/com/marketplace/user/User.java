package com.marketplace.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private String username;
    private String password;
    private UserRole role;
}
