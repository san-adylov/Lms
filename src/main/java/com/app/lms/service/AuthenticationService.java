package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.authenticationDto.AuthenticationResponse;
import com.app.lms.dto.authenticationDto.RecoveryPasswordRequest;
import com.app.lms.dto.authenticationDto.SignInRequest;

public interface AuthenticationService {

    AuthenticationResponse signIn(SignInRequest signInRequest);

    SimpleResponse sendPasswordToEmail(String emailAddress, String link);

    SimpleResponse recoveryPassword(RecoveryPasswordRequest recoveryPasswordRequest);
}
