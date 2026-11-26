package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.config.AppProperties;
import com.bosanci.recipesbackend.dto.UserDto;
import com.bosanci.recipesbackend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AppProperties properties;

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.isEnabled(),
                imageUrl(user), user.getRegion(), user.getBiography());
    }

    private String imageUrl(User user) {
        String path = user.getProfileImage();
        if (path == null || path.isBlank()) {
            return null;
        }
        String base = properties.publicBaseUrl();
        return (base.endsWith("/") ? base.substring(0, base.length() - 1) : base) + path;
    }
}