package com.example.lms.service;

import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signIn(SignInRequest signInRequest);

}