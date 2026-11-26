package com.bosanci.recipesbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Korisničko ime je obavezno.")
        @Size(min = 3, max = 30, message = "Korisničko ime mora imati između 3 i 30 karaktera.")
        @Pattern(regexp = "^[A-Za-z0-9_.-]+$",
                message = "Korisničko ime sme da sadrži samo slova, brojeve, tačku, crtu i donju crtu.")
        String username,

        @NotBlank(message = "E-pošta je obavezna.")
        @Email(message = "E-pošta nije ispravna.")
        @Size(max = 254, message = "E-pošta je predugačka.")
        String email,

        @NotBlank(message = "Lozinka je obavezna.")
        @Pattern(regexp = PasswordRules.REGEX, message = PasswordRules.MESSAGE)
        String password
) {
}
