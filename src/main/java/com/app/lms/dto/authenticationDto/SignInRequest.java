package com.app.lms.dto.authenticationDto;

import lombok.Builder;

@Builder
public record SignInRequest(String email,
                            String password) {

}
