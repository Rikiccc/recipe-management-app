package com.bosanci.recipesbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank(message = "E-pošta je obavezna.")
        @Email(message = "E-pošta nije ispravna.")
        String email
) {
}
