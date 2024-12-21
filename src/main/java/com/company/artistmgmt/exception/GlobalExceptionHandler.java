package com.company.artistmgmt.exception;

import com.company.artistmgmt.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArtistException.class)
    public ResponseEntity<BaseResponse<String>> handleArtistException(ArtistException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),  // Status code for the error
                ex.getMessage()                   // Error message
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        BaseResponse<Object> errorResponse = new BaseResponse<>(
                HttpStatus.UNAUTHORIZED.value(),  // Status code for the error
                ex.getMessage()                   // Error message
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Handle ResourceNotFoundException (user not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle ValidationException (validation errors)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationException(ValidationException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BaseResponse<String>> handleAuthException(AuthException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ArtistRuntimeException.class)
    public ResponseEntity<BaseResponse<String>> handleArtistRuntimeException(ArtistRuntimeException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedException.class)
    public ResponseEntity<BaseResponse<String>> handleUserUpdateFailedException(FailedException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<BaseResponse<String>> handleNotAllowedException(NotAllowedException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Handle invalid request body parsing (malformed JSON or missing fields)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Failed to parse request body. Please check the input format." + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericExceptions(Exception ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred." + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
