package com.bosanci.recipesbackend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Unesite korisničko ime ili e-poštu.")
        @Size(max = 254)
        String usernameOrEmail,

        @NotBlank(message = "Unesite lozinku.")
        @Size(max = 200)
        String password
) {
}
