package com.chumabanking.sentinel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice // This tells Spring to watch all Controllers for errors
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleSentinelRejection(RuntimeException ex) {
        // If the error message contains our Sentinel keyword, make it a 403 Forbidden
        if (ex.getMessage().contains("SENTINEL REJECTED")) {
            return new ResponseEntity<>(
                    Map.of("status", "SECURITY_ALERT", "message", ex.getMessage()),
                    HttpStatus.FORBIDDEN // 403 is better for security denials
            );
        }

        // For other errors, return a standard 400
        return new ResponseEntity<>(
                Map.of("status", "ERROR", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}