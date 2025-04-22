package com.maxkavun.advice;

import com.maxkavun.exception.AuthorizationServiceException;
import com.maxkavun.exception.LocationServiceException;
import com.maxkavun.exception.RegistrationServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationServiceException.class)
    public String handleLocationServiceException(LocationServiceException ex ) {
        log.error("Handled LocationServiceException: {}", ex.getMessage(), ex);
        return "error";
    }

    @ExceptionHandler(RegistrationServiceException.class)
    public String handleRegistrationServiceException(RegistrationServiceException ex ) {
        log.error("Handled RegistrationServiceException: {}", ex.getMessage(), ex);
        return "error";
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public String handleAuthorizationServiceException(AuthorizationServiceException ex) {
        log.error("Handled AuthorizationServiceException: {}", ex.getMessage(), ex);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        log.error("Handled Exception: {}", ex.getMessage(), ex);
        return "error";
    }
}
