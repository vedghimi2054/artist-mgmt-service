package com.company.artistmgmt.exception;

public class ArtistRuntimeException extends RuntimeException {
    public ArtistRuntimeException(String msg) {
        super(msg);
    }

    public ArtistRuntimeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
