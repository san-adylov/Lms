package com.example.lms.service.impl;

import com.example.lms.configs.security.JWTService;
import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;
import com.example.lms.entities.Instructor;
import com.example.lms.entities.Student;
import com.example.lms.entities.User;
import com.example.lms.enums.Role;
import com.example.lms.exeptions.BadCredentialException;
import com.example.lms.exeptions.BadRequestException;
import com.example.lms.exeptions.NotFoundException;
import com.example.lms.repository.InstructorRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JWTService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final InstructorRepository instructorRepository;

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        if (signInRequest.email().isBlank()) {
            log.error("Email is blank!" +
                    "\nMethod name: signIn");
            throw new BadRequestException("Email is blank!");
        }
        if (!userRepository.existsUserByEmail(signInRequest.email())) {
            log.error
                    (("User with email: %s not found " +
                            "\nMethod name: signIn").formatted(signInRequest.email()));
            throw new NotFoundException("User with email: %s not found".formatted(signInRequest.email()));
        }
        User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(
                () -> {
                    log.error("User with email %s not found".formatted(signInRequest.email()));
                    return new NotFoundException("User with email %s not found".formatted(signInRequest.email()));
                }
        );
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.error("Password incorrect!" +
                    "\nMethod name: signIn");
            throw new BadRequestException("Password incorrect!");
        }
        Student student = studentRepository.getStudentsByUserId(user.getId());
        if (user.getRole().equals(Role.STUDENT) && student.isBlocked()) {
            log.error("You have been blocked and you do not have access to our system!" +
                    "\nMethod name: signIn");
            throw new BadCredentialException("You have been blocked and you do not have access to our system!");
        } else if (user.getRole().equals(Role.INSTRUCTOR)) {
            Instructor instructor = instructorRepository.getInstructorByUserId(user.getId())
                    .orElseThrow(
                            () -> {
                                log.error(("Instructor with userID: %s not found" +
                                        "\nMethod name: signIn").formatted(user.getId()));
                                return new NotFoundException("Instructor not found");
                            }
                    );
            String token = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .id(instructor.getId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }
        String token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}