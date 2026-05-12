package com.example.detailed_authentication.ExceptionHandler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(HttpStatus status, String error, Map<String, String> fieldErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = error;
        this.fieldErrors = fieldErrors;
    }
}
