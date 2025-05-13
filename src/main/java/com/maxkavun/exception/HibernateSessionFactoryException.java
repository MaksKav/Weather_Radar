package com.maxkavun.exception;

public class HibernateSessionFactoryException extends RuntimeException {
    public HibernateSessionFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
