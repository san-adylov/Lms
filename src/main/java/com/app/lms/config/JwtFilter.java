package com.app.lms.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.app.lms.entities.User;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {
    final String tokenHeader = request.getHeader("Authorization");
    if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
      String token = tokenHeader.substring(7);
      if (StringUtils.hasText(token)) {
        try {
          String userName = jwtService.validateToken(token);
          User user = userRepository.getUserByEmail(userName).orElseThrow(
              () -> new NotFoundException(
                  String.format("User with email:%s does not exist", userName)));
          SecurityContextHolder.getContext()
              .setAuthentication(
                  new UsernamePasswordAuthenticationToken(
                      user.getUsername()
                      , null,
                      user.getAuthorities()
                  )
              );
        } catch (JWTVerificationException e) {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
        }
      }
    }
    filterChain.doFilter(request, response);
  }
}
