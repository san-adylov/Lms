package com.app.lms.dto.authenticationDto;

import lombok.Builder;

@Builder
public record RecoveryPasswordRequest(Long userId,
                                      String password,
                                      String repeatPassword) {

}
