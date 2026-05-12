package com.example.detailed_authentication.ExceptionHandler;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatusCode  status;

    public ApiError(){
        this.timeStamp = LocalDateTime.now();
    }
    public ApiError(HttpStatus status, String error) {
        this();
        this.error = error;
        this.status = status;
    }

}
