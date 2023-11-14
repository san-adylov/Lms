package com.app.lms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExceptionResponse {

  private HttpStatus httpStatus;
  private String ExceptionClassName;
  private String message;

}
