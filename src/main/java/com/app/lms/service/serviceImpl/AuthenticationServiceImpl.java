package com.app.lms.service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.app.lms.config.JwtService;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.authenticationDto.AuthenticationResponse;
import com.app.lms.dto.authenticationDto.RecoveryPasswordRequest;
import com.app.lms.dto.authenticationDto.SignInRequest;
import com.app.lms.entities.Instructor;
import com.app.lms.entities.Student;
import com.app.lms.entities.User;
import com.app.lms.enums.Role;
import com.app.lms.exceptions.BadCredentialException;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.InstructorRepository;
import com.app.lms.repositories.StudentRepository;
import com.app.lms.repositories.UserRepository;
import com.app.lms.service.AuthenticationService;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final StudentRepository studentRepository;

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {

        User user = userRepository.getUserByEmail(signInRequest.email())
                .orElseThrow(() -> {
                    log.error(String.format("Пользователь с адресом электронной почты: %s не найден", signInRequest.email()));
                    return new NotFoundException(
                            String.format("Пользователь с этим адресом электронной почты: %s еще не зарегистрирован", signInRequest.email()));
                });
        if (signInRequest.password().isBlank()) {
            log.error("Данный пароль пуст");
            throw new BadCredentialException("Данный пароль пуст");
        }
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.error("Неправильный пароль");
            throw new BadCredentialException("Неправильный пароль");
        }
        Student student = studentRepository.getStudentsByUserId(user.getId());
        if (user.getRole().equals(Role.STUDENT)) {
            if (student.isBlocked()) {
                log.error("Вы были заблокированы, поэтому у вас нет доступа к нашей системе");
                throw new BadCredentialException("Вы были заблокированы, поэтому у вас нет доступа к нашей системе");
            }
        }
        if (user.getRole().equals(Role.STUDENT)){
            String token = jwtService.generateToken(user);
            log.info("Процесс входа в систему с использованием электронной почты и пароля");
            return AuthenticationResponse.builder()
                    .id(student.getId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        } else if (user.getRole().equals(Role.INSTRUCTOR)) {
            Instructor instructor = instructorRepository.getInstructorByUserId(user.getId());
            String token = jwtService.generateToken(user);
            log.info("Процесс входа в систему с использованием электронной почты и пароля");
            return AuthenticationResponse.builder()
                    .id(instructor.getId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }
        String token = jwtService.generateToken(user);
        log.info("Процесс входа в систему с использованием электронной почты и пароля");
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public SimpleResponse sendPasswordToEmail(String emailAddress, String link) {

        User user = userRepository.getUserByEmail(emailAddress).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с адресом электронной почты: %s еще не зарегистрирован", emailAddress)));
        String emailContent =link + "/" + user.getId();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(emailAddress);
            helper.setSubject("Васстановление пароля");

            Context context = new Context();
            context.setVariable("link",emailContent);
            String htmlContent = templateEngine.process("forgotPassword",context);
            helper.setText(htmlContent,true);
            javaMailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
        log.info("Отправка ссылки для пароля на почту");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
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
                    .status(HttpStatus.OK)
                    .message("Успешно")
                    .build();
        } else {
            log.error("Пароли должны совпадать друг с другом");
            throw new BadRequestException("Пароли должны совпадать друг с другом");
        }
    }
}

