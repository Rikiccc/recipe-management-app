package com.bosanci.recipesbackend.dto.auth;

public final class PasswordRules {

    public static final String REGEX = "^(?=.*\\p{L})(?=.*\\d).{8,72}$";
    public static final String MESSAGE = "Lozinka mora imati 8-72 karaktera, bar jedno slovo i jednu cifru.";

    private PasswordRules() {
    }
}
