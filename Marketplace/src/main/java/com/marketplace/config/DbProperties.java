package com.marketplace.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties("spring.datasource")
public class DbProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
