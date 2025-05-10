package com.lynki.lynki.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.UsernameNotFound.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException exception) {
        return buildResponse(exception, HttpStatus.NOT_FOUND, "USERNAME_NOT_FOUND");
    }

    @ExceptionHandler(UserException.UsernameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UserException.UsernameAlreadyExists exception) {
        return buildResponse(exception, HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS");
    }

    private ResponseEntity<ErrorResponse> buildResponse(RuntimeException exception, HttpStatus status, String code)  {
        ErrorResponse errorResponse = new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), exception.getMessage(), code);
        return new ResponseEntity<>(errorResponse, status);
    }
}
