package com.app.lms.service.serviceImpl;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.studentDto.*;
import com.app.lms.entities.Course;
import com.app.lms.entities.Group;
import com.app.lms.entities.Student;
import com.app.lms.entities.User;
import com.app.lms.enums.Role;
import com.app.lms.exceptions.AlreadyExistException;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.CourseRepository;
import com.app.lms.repositories.GroupRepository;
import com.app.lms.repositories.StudentRepository;
import com.app.lms.repositories.UserRepository;
import com.app.lms.service.StudentService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${spring.password.characters}")
    private String characters;

    public String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            char randomChar = characters.charAt(randomIndex);
            password.append(randomChar);
        }
        return password.toString();
    }
    private void sendPasswordToEmail(String recipientEmail, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
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
    public SimpleResponse saveStudent(Long groupId, StudentRequest studentRequest) {
        String password = generatePassword();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error(String.format("Группа с идентификатором: %s не найдена", groupId));
            return new NotFoundException(String.format("Группа с идентификатором: %s не найдена", groupId));
        });
        Student student = new Student();
        User user = new User();
        user.setFirstName(studentRequest.firstName());
        user.setLastName(studentRequest.lastName());
        user.setEmail(studentRequest.email());
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(studentRequest.phoneNumber());
        user.setRole(Role.STUDENT);
        userRepository.save(user);
        student.setStudyFormat(studentRequest.studyFormat());
        student.setUser(user);
        student.setGroup(group);
        studentRepository.save(student);
        sendPasswordToEmail(user.getEmail(), password);
        log.info("Студент успешно сохранен");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно сохранено")
                .build();
    }

    @Override
    public PaginationStudentResponse getAllStudentByCourseId(Long courseId, int pageSize, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<StudentResponse> allStudent = studentRepository.getAllStudentsByCourseId(courseId, pageable);
        log.info("Список успешно принятых студентов");
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", courseId));
        });
        if (allStudent.isEmpty()) {
            return PaginationStudentResponse
                    .builder()
                    .studentResponses(new ArrayList<>())
                    .page(allStudent.getNumber() + 1)
                    .size(allStudent.getSize())
                    .build();
        }
        return PaginationStudentResponse
                .builder()
                .studentResponses(allStudent.getContent())
                .page(allStudent.getNumber() + 1)
                .size(allStudent.getSize())
                .quantityOfStudents(course.getGroup().getStudents().size())
                .build();
    }

    @Override
    public PaginationStudentResponse getAllStudents(int pageSize, int currenPage) {
        Pageable pageable = PageRequest.of(currenPage - 1, pageSize);
        Page<StudentResponse> allStudent = studentRepository.getAllStudents(pageable);
        log.info("Список успешно принятых студентов");
        List<Student> all = studentRepository.findAll();

        return PaginationStudentResponse
                .builder()
                .studentResponses(allStudent.getContent())
                .page(allStudent.getNumber() + 1)
                .size(allStudent.getSize())
                .quantityOfStudents(all.size())
                .build();
    }

    @Override
    public List<CourseResponse> getById(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> {
            log.error(String.format("Студент с идентификатором: %s не найден", studentId));
            return new NotFoundException(String.format("Студент с идентификатором: %s не найден", studentId));
        });
        return courseRepository.getAllStudentCourses(studentId);
    }

    @Override
    public SimpleResponse updateStudent(Long studentId, StudentRequest1 studentRequest) {
        String password = generatePassword();
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            log.error(String.format("Студент с идентификатором: %s не найден", studentId));
            return new NotFoundException(String.format("Студент с идентификатором: %s не найден", studentId));
        });
        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с идентификатором: %s не найден", studentId)));
        user.setFirstName(studentRequest.firstName());
        user.setLastName(studentRequest.lastName());
        user.setEmail(studentRequest.email());
        user.setPhoneNumber(studentRequest.phoneNumber());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        student.setStudyFormat(studentRequest.studyFormat());
        student.setUser(user);
        studentRepository.save(student);
        sendPasswordToEmail(user.getEmail(), password);
        log.info("Студент успешно обновлен");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно обновлено")
                .build();
    }

    @Override
    public SimpleResponse deleteStudent(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> {
            log.error(String.format("Студент с идентификатором: %s не найден", studentId));


            return new NotFoundException(String.format("Студент с идентификатором: %s не найден", studentId));
        });
        studentRepository.deleteById(studentId);
        log.info("Студент успешно удален");

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }

    @Override
    public SimpleResponse deleteStudentFromGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error(String.format("Группа с идентификатором: %s не найдена", groupId));
            return new NotFoundException(String.format("Группа с идентификатором: %s не найдена", groupId));
        });
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            log.error(String.format("Студент с идентификатором: %s не найден", studentId));
            return new NotFoundException(String.format("Студент с идентификатором: %s не найден", studentId));
        });
        if (group.getStudents().contains(student)) {
            group.getStudents().remove(student);
        } else {
            log.error(String.format("Студент с идентификатором: %s не содержится в этой группе", studentId));
            throw new BadRequestException(String.format("Студента с идентификатором:%s нет в этом курсе", studentId));
        }

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }

    @Override
    public SimpleResponse importExcel(Long groupId, MultipartFile multipartFile) throws IOException {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> {
                    log.error("Группа с идентификатором {} не найдена", groupId);
                    return new NotFoundException("Группа с: " + groupId + "идентификатором не найден!!!");
                });

        if (!multipartFile.isEmpty()) {
            PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().build();
            InputStream inputStream = multipartFile.getInputStream();
            List<StudentExcelRequest> excelRequests = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, StudentExcelRequest.class, options);
            for (StudentExcelRequest studentExcelRequest : excelRequests) {
                String password = generatePassword();
                if (userRepository.existsByEmail(studentExcelRequest.getEmail())) {
                    log.info("Студент с:" + studentExcelRequest.getEmail() + " существует!");
                    throw new AlreadyExistException("Студент с: " + studentExcelRequest.getEmail() + " существует! ");
                }
                User user = User.builder()
                        .firstName(studentExcelRequest.getFirstName())
                        .lastName(studentExcelRequest.getLastName())
                        .email(studentExcelRequest.getEmail())
                        .password(passwordEncoder.encode(password))
                        .phoneNumber(studentExcelRequest.getPhoneNumber())
                        .role(Role.STUDENT)
                        .build();
                userRepository.save(user);
                sendPasswordToEmail(user.getEmail(), password);
                Student student = Student.builder()
                        .studyFormat(studentExcelRequest.getStudyFormat())
                        .isBlocked(false)
                        .group(group)
                        .build();
                group.setStudents(List.of(student));
                studentRepository.save(student);
                student.setUser(user);
            }
        }
        log.info("Студенты успешно сохранены!");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Студенты из файла Excel успешно добавлены!")
                .build();
    }
}
