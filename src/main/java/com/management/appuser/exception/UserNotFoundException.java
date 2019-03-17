package com.management.appuser.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException {
    private final String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }
}
