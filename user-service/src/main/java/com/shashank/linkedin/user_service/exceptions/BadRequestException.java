package com.shashank.linkedin.user_service.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
