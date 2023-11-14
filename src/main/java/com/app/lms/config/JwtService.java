package com.app.lms.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  @Value("${spring.jwt.secret_key}")
  private String SECURITY_KEY;

  public String generateToken(UserDetails userDetails) {
    return JWT.create()
        .withClaim("username", userDetails.getUsername())
        .withIssuedAt(new Date())
        .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(3).toInstant()))
        .sign(Algorithm.HMAC256(SECURITY_KEY));
  }

  public String validateToken(String token) {
    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECURITY_KEY))
        .build();
    DecodedJWT decodedJWT = jwtVerifier.verify(token);
    return decodedJWT.getClaim("username").asString();
  }
}
