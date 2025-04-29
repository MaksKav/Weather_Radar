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

    @ExceptionHandler({
            LocationServiceException.class,
            RegistrationServiceException.class,
            AuthorizationServiceException.class,
            Exception.class
    })
    public String handleException(Exception ex) {
        log.error("Handled {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return "error";
    }

}
