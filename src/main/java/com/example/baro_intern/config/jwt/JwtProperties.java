package com.example.baro_intern.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    Secret secret,
    Token token
) {
    public record Secret(String key) {}
    public record Token(String prefix, long expiration) {}
}
