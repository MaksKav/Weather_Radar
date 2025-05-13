package com.maxkavun.exception;

public class AuthorizationServiceException extends RuntimeException {
    public AuthorizationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
