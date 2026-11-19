package com.bosanci.recipesbackend.exception;

/**
 * Throw when resource that is requested doesnt exist in the base
 * Mapped to http 404
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " sa id-jem " + id + " ne postoji.");
    }
}
