package com.example.lms.configs.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.lms.entities.User;
import com.example.lms.exeptions.NotFoundException;
import com.example.lms.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String tokenHeader = request.getHeader("Authorization");

        if (isValidBearerToken(tokenHeader)) {
            String token = extractToken(tokenHeader);

            if (StringUtils.hasText(token)) {
                try {
                    String username = jwtService.validateToken(token);
                    User user = userRepository.getUserByEmail(username)
                            .orElseThrow(() -> {
                                log.error("User with email: %s not found".formatted(username));
                                return new NotFoundException("User with email: %s not found".formatted(username));
                            });

                    setAuthentication(user);

                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token!");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidBearerToken(String tokenHeader) {
        return tokenHeader != null && tokenHeader.startsWith("Bearer ");
    }

    private String extractToken(String tokenHeader) {
        return tokenHeader.substring(7);
    }

    private void setAuthentication(User user) {
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                user.getAuthorities()
                        )
                );
    }

}
