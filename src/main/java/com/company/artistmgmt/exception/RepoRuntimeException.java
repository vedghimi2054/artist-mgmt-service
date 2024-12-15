package com.company.artistmgmt.exception;

public class RepoRuntimeException extends RuntimeException {
    public RepoRuntimeException(String msg) {
        super(msg);
    }

    public RepoRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public RepoRuntimeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
