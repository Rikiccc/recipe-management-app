package com.bosanci.recipesbackend.exception;

/** Credentials are missing or wrong. Mapped to HTTP 401. */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
