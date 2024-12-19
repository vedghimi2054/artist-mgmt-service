package com.company.artistmgmt.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
    private boolean error;
    private T dataResponse;

    public BaseResponse(boolean success, T dataResponse) {
        this.success = success;
        this.timestamp = LocalDateTime.now();
        this.error = false;
        this.dataResponse = dataResponse;
        this.statusCode = 200;
        this.message = "Request was successful";
    }

    // Constructor for error
    public BaseResponse(int statusCode, String message) {
        this.success = false;
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.statusCode = statusCode;
        this.message = message;
    }
}
