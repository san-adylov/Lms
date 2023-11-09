package com.example.lms.service;

import com.example.lms.dto.request.authentication.RecoveryPasswordRequest;
import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;
import com.example.lms.dto.response.simple.SimpleResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {

    AuthenticationResponse signIn(SignInRequest signInRequest);

    SimpleResponse sendPasswordToEmail(String email, String link) throws MessagingException;

    SimpleResponse recoveryPassword(RecoveryPasswordRequest recoveryPasswordRequest);



}