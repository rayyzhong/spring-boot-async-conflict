package com.rayhong.spring.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EcareException.class)
    public ResponseEntity<ApiErrorResponse> handleEcareException(
            EcareException ex) {
        ApiErrorResponse response =
                new ApiErrorResponse("error-001",
                        ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EcareBusyException.class)
    public ResponseEntity<ApiErrorResponse> handleEcareBusyException(
            EcareBusyException ex) {
        ApiErrorResponse response =
                new ApiErrorResponse("error-002",
                        ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception ex) {
        ApiErrorResponse response =
                new ApiErrorResponse("error-500",
                        ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}