package com.example.demo.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DbProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
