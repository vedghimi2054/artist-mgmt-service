package com.company.artistmgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
    private boolean error;

    private T dataResponse;
    private Map<String, Object> meta;

    public BaseResponse() {
        this.meta = new HashMap<>();
    }

    public BaseResponse(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public BaseResponse(boolean success, T dataResponse) {
        super();
        this.success = success;
        this.timestamp = LocalDateTime.now();
        this.error = false;
        this.dataResponse = dataResponse;
        this.statusCode = 200;
        this.message = "Request was successful";
    }

    // Constructor for error
    public BaseResponse(int statusCode, String message) {
        super();
        this.success = false;
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.statusCode = statusCode;
        this.message = message;
    }

    public void addMeta(String key, Object value) {
        if (meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.put(key, value);
    }
}
