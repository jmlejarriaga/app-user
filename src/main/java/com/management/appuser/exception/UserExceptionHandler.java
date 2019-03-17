package com.management.appuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserBadRequestException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(UserBadRequestException e) {
        return ResponseEntity.status(BAD_REQUEST).body(buildErrorResponse(BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(buildErrorResponse(NOT_FOUND, e.getMessage()));
    }

    private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus, message);
    }
}
