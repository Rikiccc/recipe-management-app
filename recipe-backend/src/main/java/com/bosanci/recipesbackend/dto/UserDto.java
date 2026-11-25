package com.bosanci.recipesbackend.dto;
import com.bosanci.recipesbackend.model.Role;

public record UserDto(
        Long id,
        String username,
        String email,
        Role role,
        boolean enabled,
        String profileImage,
        String region,
        String biography
) {
}
