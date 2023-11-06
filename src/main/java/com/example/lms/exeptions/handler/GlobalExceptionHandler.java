package com.example.lms.exeptions.handler;

import com.example.lms.dto.response.simple.ExceptionResponse;
import com.example.lms.exeptions.BadCredentialException;
import com.example.lms.exeptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Executable;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse notFoundHandler(NotFoundException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handlerBadCredential(BadCredentialException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
}
