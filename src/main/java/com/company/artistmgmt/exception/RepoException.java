package com.company.artistmgmt.exception;

public class RepoException extends Exception {
    public RepoException(String msg) {
        super(msg);
    }

    public RepoException(Throwable throwable) {
        super(throwable);
    }

    public RepoException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
