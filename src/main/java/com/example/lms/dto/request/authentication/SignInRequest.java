package com.example.lms.dto.request.authentication;

import lombok.Builder;

@Builder
public record SignInRequest(
        String email,

        String password
) {
}