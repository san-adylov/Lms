package com.example.lms.dto.response.authentication;

import com.example.lms.enums.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        Long id,
        String email,
        String token,
        Role role
) {
}