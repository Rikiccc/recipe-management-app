package com.bosanci.recipesbackend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Error format that backend sends back to client
 *
 * @param timestamp   time of error
 * @param status      HTTP status code (ie. 400, 404, 500)
 * @param error       name (ie. "Not Found")
 * @param message     message
 * @param path        request path
 * @param fieldErrors field errors (only code 400)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null);
    }

    public static ErrorResponse withFieldErrors(int status, String error, String message, String path,
                                                Map<String, String> fieldErrors) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, fieldErrors);
    }
}
