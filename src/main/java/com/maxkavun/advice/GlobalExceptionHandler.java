package com.maxkavun.advice;

import com.maxkavun.exception.LocationServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationServiceException.class)
    public String handleLocationServiceException(LocationServiceException ex , Model model) {
        log.error("Handled LocationServiceException: {}", ex.getMessage(), ex);
        return "error";
    }



}
