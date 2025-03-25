package com.maxkavun.exception;

public class HibernateInitializationException extends RuntimeException {
    public HibernateInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
