package com.bosanci.recipesbackend.security;

import com.bosanci.recipesbackend.model.Role;

import java.time.Instant;

public record AuthenticatedUser(Long id, String username, Role role, String tokenId, Instant tokenExpiresAt) {
}
