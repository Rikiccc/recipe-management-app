package com.bosanci.recipesbackend.exception;

/** User is known but is not allowed to do this (ie. account not activated). Mapped to HTTP 403. */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
