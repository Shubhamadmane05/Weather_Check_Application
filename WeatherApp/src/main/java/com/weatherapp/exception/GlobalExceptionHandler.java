package com.weatherapp.exception;


import com.weatherapp.utility.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        // Handle Custom City Not Found Error
        @ExceptionHandler(CityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCityNotFound(CityNotFoundException ex, WebRequest request) {

            ErrorResponse error = ErrorResponse.builder()
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getDescription(false))
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Handle JSON Parsing / Conversion Errors
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {

            ErrorResponse error = ErrorResponse.builder()
                    .message("Internal Server Error: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getDescription(false))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Handle validation errors if DTOs use @Valid
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {

            ErrorResponse error = ErrorResponse.builder()
                    .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getDescription(false))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

