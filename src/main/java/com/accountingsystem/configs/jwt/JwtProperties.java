package com.accountingsystem.configs.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtProperties {
    private String secret;
    private int expirationMs;
}
