package com.example.lms.dto.request.authentication;

public record SignInRequest(
        String email,

        String password
) {
}