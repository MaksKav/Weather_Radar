package com.maxkavun.exception;

public class IncorrectUserLoginException extends RuntimeException {

    public IncorrectUserLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
