package com.bosanci.recipesbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, String frontendUrl, String publicBaseUrl, Mail mail, Upload upload) {

    public record Jwt(String secret, long expirationMinutes) {
    }

    public record Mail(String from) {
    }

    public record Upload(String dir) {
    }
}