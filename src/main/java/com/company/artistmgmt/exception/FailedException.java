package com.company.artistmgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FailedException extends RuntimeException {
    public FailedException(String message) {
        super(message);
    }
}
