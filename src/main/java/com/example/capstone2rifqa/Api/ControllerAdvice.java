package com.example.capstone2rifqa.Api;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse> apiException(ApiException e) {
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> validationException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> mismatchException(MethodArgumentTypeMismatchException e) {
        String message = "Invalid value: " + e.getValue();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> unreadableBodyException(HttpMessageNotReadableException e) {
        String message = "Invalid request body, please check your fields and values";
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> notFoundException(NoResourceFoundException e) {
        String message = "Endpoint not found: /" + e.getResourcePath();
        return ResponseEntity.status(404).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = "Method " + e.getMethod() + " is not supported for this endpoint";
        return ResponseEntity.status(405).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> mediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        String message = "Request body must be JSON";
        return ResponseEntity.status(415).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> onDataIntegrityViolation(DataIntegrityViolationException e) {
        System.out.println("Database error: " + e.getMostSpecificCause().getMessage());
        String message = "The data could not be saved, please check that your values are valid and not too long";
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> unexpectedException(Exception e) {
        System.out.println("Unexpected error: " + e);
        String message = "Something went wrong, please try again later";
        return ResponseEntity.status(500).body(new ApiResponse(message));
    }
}