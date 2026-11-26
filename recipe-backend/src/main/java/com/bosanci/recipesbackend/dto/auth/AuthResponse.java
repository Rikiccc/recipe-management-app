package com.bosanci.recipesbackend.dto.auth;

import com.bosanci.recipesbackend.dto.UserDto;

import java.time.Instant;

public record AuthResponse(String token, String tokenType, Instant expiresAt, UserDto user) {
}
