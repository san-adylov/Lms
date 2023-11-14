package com.app.lms.service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.instructorDto.InstructorRequest;
import com.app.lms.dto.instructorDto.InstructorResponse;
import com.app.lms.dto.instructorDto.InstructorUpdateRequest;
import com.app.lms.entities.Course;
import com.app.lms.entities.Instructor;
import com.app.lms.entities.User;
import com.app.lms.enums.Role;
import com.app.lms.exceptions.BadCredentialException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.CourseRepository;
import com.app.lms.repositories.InstructorJdbcTemplateQueryRepository;
import com.app.lms.repositories.InstructorRepository;
import com.app.lms.repositories.UserRepository;
import com.app.lms.service.InstructorService;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final CourseRepository courseRepository;
    private final InstructorJdbcTemplateQueryRepository queryRepository;
    private final TemplateEngine templateEngine;
    private final Random random = new SecureRandom();
    @Value("${spring.password.characters}")
    private String characters;
    @Value("${spring.mail.username}")
    private String fromEmail;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.getUserByEmail(name)
                .orElseThrow(() -> new NotFoundException("Пользователь с ником:" + name + " не найдено"));
    }

    @Override
    public List<InstructorResponse> getAllInstructors() {
        log.info("Список успешно полученных инструкторов");
        return jdbcTemplate.query(queryRepository.getAllInstructors(),
                (rs, rowNum) -> new InstructorResponse(
                        rs.getLong("id"),
                        rs.getString("fullName"),
                        rs.getString("specialization"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private void sendPasswordToEmail(String emailAddress, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(emailAddress);
            helper.setSubject("Password");
            Context context = new Context();
            context.setVariable("password",password);
            String htmlContent = templateEngine.process("newPassword",context);
            helper.setText(htmlContent,true);
            javaMailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public SimpleResponse saveInstructor(InstructorRequest instructorRequest) {
        String password = generatePassword();
        User user = new User();
        user.setFirstName(instructorRequest.firstName());
        user.setLastName(instructorRequest.lastName());
        user.setPhoneNumber(instructorRequest.phoneNumber());
        user.setEmail(instructorRequest.email());
        user.setRole(Role.INSTRUCTOR);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        Instructor instructor = new Instructor();
        instructor.setSpecialization(instructorRequest.specialization());
        instructorRepository.save(instructor);
        instructor.setUser(user);
        sendPasswordToEmail(user.getEmail(), password);
        log.info("Новый инструктор успешно сохранен!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse updateInstructor(Long id, InstructorUpdateRequest instructorUpdateRequest) {
        String password = generatePassword();
        Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> {
            log.error("Инструктор с идентификатором {} не найден", id);
            return new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", id));
        });

        User user = userRepository.findById(instructor.getUser().getId()).orElseThrow(() -> {
            log.error("Пользователь с идентификатором {} не найден", instructor.getUser().getId());
            return new NotFoundException(String.format("Пользователь с идентификатором: %s не найден", instructor.getUser().getId()));
        });
        if (!instructorUpdateRequest.firstName().equals(user.getFirstName())){
        user.setFirstName(instructorUpdateRequest.firstName());
        }
        if (!instructorUpdateRequest.lastName().equals(user.getLastName())){
        user.setLastName(instructorUpdateRequest.lastName());
        }
        if (!instructorUpdateRequest.email().equals(user.getEmail())){
        user.setEmail(instructorUpdateRequest.email());
        }
        if (!instructorUpdateRequest.phoneNumber().equals(user.getPhoneNumber())){
        user.setPhoneNumber(instructorUpdateRequest.phoneNumber());
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        if (!instructorUpdateRequest.specialization().equals(instructor.getSpecialization())){
        instructor.setSpecialization(instructorUpdateRequest.specialization());
        }
        instructorRepository.save(instructor);
        sendPasswordToEmail(user.getEmail(), password);
        log.info("Инструктор успешно обновлен!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse deleteInstructor(Long id) {
        if (instructorRepository.existsById(id)) {
            Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> {
                log.error("Инструктор с идентификатором {} не найден", id);
                return new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", id));
            });
            for (Course c:instructor.getCourses()) {
                c.getInstructors().remove(instructor);
            }
            instructorRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", id));
        }
        log.info("Удаление инструктора по идентификатору {} успешно выполнено", id);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

  @Override
  public List<CourseResponse> getInstructorById(Long id) {
    Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> {
      log.info("Инструктор с идентификатором {} не найден", id);
        return new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", id));
    });
    User user = getAuthentication();
    if (instructor.getUser().equals(user)) {
      return courseRepository.courses(id);
    } else {
      throw new BadCredentialException("Hе допустимое значение");
    }
}
}
