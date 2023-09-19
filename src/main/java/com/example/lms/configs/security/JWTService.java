package com.example.lms.configs.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTService {

  @Value("${secret_key}")
  private static String SECRET_KEY;

  public String generateToken(UserDetails userDetails) {
    return JWT.create()
        .withClaim("username", userDetails.getUsername())
        .withIssuedAt(new Date())
        .withExpiresAt(Date.from(ZonedDateTime.now().plusWeeks(2).toInstant()))
        .sign(Algorithm.HMAC512(SECRET_KEY));
  }


}
