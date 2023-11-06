package com.example.lms.exeptions.handler;

import com.example.lms.dto.response.simple.ExceptionResponse;
import com.example.lms.exeptions.BadCredentialException;
import com.example.lms.exeptions.BadRequestException;
import com.example.lms.exeptions.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse notFoundHandler(NotFoundException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .className(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handlerBadCredential(BadCredentialException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .className(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handlerBadRequest(BadRequestException e) {
        return ExceptionResponse
                .builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .className(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handlerMethodNotValid(MethodArgumentNotValidException e) {
        List<String> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ExceptionResponse
                .builder()
                .message(errors.toString())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .className(e.getClass().getSimpleName())
                .build();
    }
}
