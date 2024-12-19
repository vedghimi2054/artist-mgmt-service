package com.company.artistmgmt.exception;

public class ArtistException extends Exception {
    public ArtistException(String msg) {
        super(msg);
    }

    public ArtistException(Throwable throwable) {
        super(throwable);
    }

    public ArtistException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
