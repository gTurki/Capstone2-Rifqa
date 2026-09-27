package com.example.capstone2rifqa.Api;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    // Errors thrown by the services
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse> onApiException(ApiException e) {
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // @Valid failures, e.g. name too short or invalid email
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> onValidationException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // Wrong type in the URL, e.g. /get/abc instead of /get/5
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> onTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Invalid value: " + e.getValue();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    // Broken JSON or wrong enum in the body, e.g. "sleepSchedule": "early"
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> onUnreadableBody(HttpMessageNotReadableException e) {
        String message = "Invalid request body, please check your fields and values";
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }
}