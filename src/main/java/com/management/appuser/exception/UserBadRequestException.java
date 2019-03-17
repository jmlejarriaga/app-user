package com.management.appuser.exception;

import lombok.Data;

@Data
public class UserBadRequestException extends RuntimeException {
    private final String message;

    public UserBadRequestException(String message) {
        this.message = message;
    }
}
