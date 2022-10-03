package com.marketplace.repository.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DbUser {
    private String username;
    private String password;
    private UserRole role;
}
