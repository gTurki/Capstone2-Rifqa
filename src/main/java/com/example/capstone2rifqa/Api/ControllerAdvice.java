package com.example.capstone2rifqa.Api;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    // for my custom errors
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse> apiException(ApiException e) {
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // for @Valid Exceptions
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> validationException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // for the wrong type in path variable
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> onTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Invalid value: " + e.getValue();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // for broken json or wrong enum
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> onUnreadableBody(HttpMessageNotReadableException e) {
        String message = "Invalid request body, please check your fields and values";
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }
}