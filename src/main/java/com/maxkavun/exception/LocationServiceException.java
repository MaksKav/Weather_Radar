package com.maxkavun.exception;

public class LocationServiceException extends RuntimeException{
    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationServiceException(String message) {
        super(message);
    }
}
