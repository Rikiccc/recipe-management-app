package com.bosanci.recipesbackend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TokenRequest(
        @NotBlank(message = "Token je obavezan.")
        @Size(max = 200)
        String token
) {
}
