package com.app.lms.exceptions.handler;

import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.app.lms.exceptions.AlreadyExistException;
import com.app.lms.exceptions.BadCredentialException;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.ExceptionResponse;
import com.app.lms.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handlerNotFoundException(NotFoundException e) {

        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

    @ExceptionHandler({BadCredentialException.class,})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handlerBadCredentialException(BadCredentialException e) {

        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleNotAlreadyExist(AlreadyExistException e) {
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

      List<String> errors = e
              .getBindingResult()
              .getFieldErrors()
              .stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .toList();

      return ExceptionResponse
              .builder()
              .message(errors.toString())
              .httpStatus(HttpStatus.CONFLICT)
              .ExceptionClassName(e.getClass().getSimpleName())
              .build();
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handlerBadRequestException(BadRequestException e) {

    return new ExceptionResponse(
        HttpStatus.BAD_REQUEST,
        e.getClass().getSimpleName(),
        e.getMessage()
    );
  }
}