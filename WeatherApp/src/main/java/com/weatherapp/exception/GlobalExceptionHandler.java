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

        // Handle Custom
        @ExceptionHandler(CityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCityNotFound(CityNotFoundException ex){
            ErrorResponse error = getErrorResponseObject(ex, HttpStatus.NOT_FOUND.value(), "City Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

            ErrorResponse error = getErrorResponseObject(ex, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error Please Check Connection");

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        public static ErrorResponse getErrorResponseObject(Exception ex, int code, String message){
            ErrorResponse error = new ErrorResponse();
            error.setError(ex.getMessage());
            error.setStatus(code);
            error.setMessage(message);
            error.setTimestamp(LocalDateTime.now());
            return error;
        }
    }

