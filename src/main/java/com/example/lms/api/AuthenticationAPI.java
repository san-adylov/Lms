package com.example.lms.api;

import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;
import com.example.lms.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "API for sign in management")
public class AuthenticationAPI {

    private final AuthenticationService authenticationService;

    @PostMapping("/signIn")
    @Operation(summary = "Sign in", description = "Sign in with email")
    public AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }


}