package com.example.lms.dto.request.authentication;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record SignInRequest(
        String email,

        String password
) {
}