package com.example.lms.dto.request.authentication;

import lombok.Builder;

@Builder
public record RecoveryPasswordRequest(
        Long userId,
        String password,
        String repeatPassword
) {
}
