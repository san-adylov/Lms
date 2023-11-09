package com.example.lms.service.impl;

import com.example.lms.configs.security.JWTService;
import com.example.lms.dto.request.authentication.RecoveryPasswordRequest;
import com.example.lms.dto.request.authentication.SignInRequest;
import com.example.lms.dto.response.authentication.AuthenticationResponse;
import com.example.lms.dto.response.simple.SimpleResponse;
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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.PasswordAuthentication;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${spring.email.username}")
    private String fromEmail;

    private final JWTService jwtService;

    private final TemplateEngine templateEngine;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final InstructorRepository instructorRepository;

    @Bean
    private JavaMailSender javaMailSender(){
        return new JavaMailSenderImpl();
    }


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

    @Override
    public SimpleResponse sendPasswordToEmail(String email, String link) throws MessagingException {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> {
                    log.error("User with email address %s is not registered".formatted(email));
                    return new NotFoundException("User with email address %s is not registered".formatted(email));
                });
        String emailContent = link + "/" + user.getId();
        try {
            MimeMessage message = javaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Password recovery");

            Context context = new Context();
            context.setVariable("link",emailContent);
            String htmlContent = templateEngine.process("forgotPassword",context);
            helper.setText(htmlContent,true);
            javaMailSender().send(message);

        }catch (MessagingException e) {
            throw new MessagingException();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success")
                .build();
    }

    @Override
    public SimpleResponse recoveryPassword(RecoveryPasswordRequest recoveryPasswordRequest) {
        User user = userRepository.findById(recoveryPasswordRequest.userId()).orElseThrow(() -> {
            log.error(String.format("Пользователь с идентификатором: %s не найден", recoveryPasswordRequest.userId()));
            return new NotFoundException(
                    String.format("Пользователь с идентификатором: %s не найден", recoveryPasswordRequest.userId()));
        });
        if (recoveryPasswordRequest.password().equals(recoveryPasswordRequest.repeatPassword())) {
            user.setPassword(passwordEncoder.encode(recoveryPasswordRequest.password()));
            log.info("пароль восстановления");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Успешно")
                    .build();
        } else {
            log.error("Пароли должны совпадать друг с другом");
            throw new BadRequestException("Пароли должны совпадать друг с другом");
        }
    }
}