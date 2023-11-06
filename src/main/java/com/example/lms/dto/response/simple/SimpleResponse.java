package com.example.lms.dto.response.simple;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record SimpleResponse(
        String message,
        HttpStatus httpStatus
) {}