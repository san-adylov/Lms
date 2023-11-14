package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.authenticationDto.AuthenticationResponse;
import com.app.lms.dto.authenticationDto.RecoveryPasswordRequest;
import com.app.lms.dto.authenticationDto.SignInRequest;
import com.app.lms.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication API", description = "API for sign in management")
public class AuthApi {

  private final AuthenticationService authenticationService;

  @PostMapping("/signIn")
  @Operation(summary = "Sign in", description = "Sign in with email")
  public AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest) {
    return authenticationService.signIn(signInRequest);
  }

  @PostMapping("/sendEmail")
  @Operation(summary = "Send email", description = "Send link to email")
  public SimpleResponse sendLinkToEmail(@RequestParam String emailAddress, @RequestParam String link) {
    return authenticationService.sendPasswordToEmail(emailAddress, link);
  }

  @PostMapping("/recover-password/{userId}")
  @Operation(summary = "Recovery password", description = "Recovery password by student through email")
  public SimpleResponse recoveryPassword( @RequestBody RecoveryPasswordRequest recoveryPasswordRequest) {
    return authenticationService.recoveryPassword(recoveryPasswordRequest);
  }
}
