package com.example.lms.dto.response.simple;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(
        String message,
        HttpStatus httpStatus,

        String className
) {
}
