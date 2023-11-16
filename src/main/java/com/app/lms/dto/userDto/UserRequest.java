package com.app.lms.dto.userDto;

import lombok.Builder;

@Builder
public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber
) {
}
