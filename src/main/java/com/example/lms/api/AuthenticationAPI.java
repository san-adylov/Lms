package com.example.lms.api;

import com.example.lms.dto.request.authentication.RecoveryPasswordRequest;
import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;
import com.example.lms.dto.response.simple.SimpleResponse;
import com.example.lms.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sendEmail")
    @Operation(summary = "Send email", description = "Send link to email")
    public SimpleResponse sendLinkToEmail(@RequestParam String emailAddress, @RequestParam String link) throws MessagingException {
        return authenticationService.sendPasswordToEmail(emailAddress, link);
    }

    @PostMapping("/recover-password/{userId}")
    @Operation(summary = "Recovery password", description = "Recovery password by student through email")
    public SimpleResponse recoveryPassword( @RequestBody RecoveryPasswordRequest recoveryPasswordRequest) {
        return authenticationService.recoveryPassword(recoveryPasswordRequest);
    }


}