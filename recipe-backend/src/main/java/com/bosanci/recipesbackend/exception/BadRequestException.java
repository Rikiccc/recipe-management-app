package com.bosanci.recipesbackend.exception;

/**
 * Thrown when request is not valid from business logic viewpoint
 * (ie. name already exists). Mapped to HTTP 400.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
