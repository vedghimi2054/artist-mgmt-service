package com.company.artistmgmt.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String msg) {
        super(msg);
    }

    public PermissionDeniedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
