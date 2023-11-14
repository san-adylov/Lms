package com.app.lms.dto.authenticationDto;

import lombok.Builder;
import com.app.lms.enums.Role;

@Builder
public record AuthenticationResponse(Long id,
                                     String email,
                                     String token,
                                     Role role) {

}
