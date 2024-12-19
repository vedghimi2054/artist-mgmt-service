package com.company.artistmgmt.exception;

import com.company.artistmgmt.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericExceptions(Exception ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
