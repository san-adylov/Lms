package com.app.lms.exceptions;

public class BadRequestException  extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}