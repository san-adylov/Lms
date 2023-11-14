package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.AssigningInstructorsRequest;
import com.app.lms.dto.courseDto.CourseRequest;
import com.app.lms.dto.courseDto.CourseResponseGroup;
import com.app.lms.dto.groupDto.GroupResponseCourse;
import com.app.lms.dto.instructorDto.InstructorResponseGroup;
import com.app.lms.dto.studentDto.StudentResponse;
import com.app.lms.entities.Course;
import com.app.lms.entities.Group;
import com.app.lms.entities.Instructor;
import com.app.lms.exceptions.AlreadyExistException;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.CourseRepository;
import com.app.lms.repositories.GroupRepository;
import com.app.lms.repositories.InstructorRepository;
import com.app.lms.repositories.StudentRepository;
import com.app.lms.service.CourseService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Override
    public SimpleResponse saveCourse(CourseRequest courseRequest) {
        Course existingCourse = courseRepository.findByCourseName(courseRequest.getCourseName());
        if (existingCourse != null) {
            log.info("Название курса уже существует! {} ", courseRequest.getCourseName());
            throw new BadRequestException("Название курса уже существует!");
        }

        Course course = new Course();
        course.setCourseName(courseRequest.getCourseName());
        course.setImage(courseRequest.getImage());
        course.setDescription(courseRequest.getDescription());
        course.setDate(LocalDate.now());
        course.setDateOfGraduation(courseRequest.getDateOfGraduation());
        courseRepository.save(course);
        log.info("Курс успешно сохранен!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Курс с идентификатором: " + course.getCourseName() + " сохранен"))
                .build();
    }

    @Override
    public List<CourseResponseGroup> getAll() {
        List<CourseResponseGroup> courseResponses = new ArrayList<>();
        List<Course> courses = courseRepository.getAllCoursesWithGroups();

        for (Course course : courses) {
            CourseResponseGroup responseGroup = new CourseResponseGroup();
            responseGroup.setId(course.getId());
            responseGroup.setCourseName(course.getCourseName());
            responseGroup.setImage(course.getImage());
            responseGroup.setDescription(course.getDescription());
            responseGroup.setDateOfGraduation(course.getDateOfGraduation());

            Group group = course.getGroup();
            if (group != null) {
                responseGroup.setGroupId(group.getId());
                responseGroup.setGroupName(group.getGroupName());
            } else {
                responseGroup.setGroupId(null);
                responseGroup.setGroupName(null);
            }

            courseResponses.add(responseGroup);
        }

        return courseResponses;
    }

    @Override
    public SimpleResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", id));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", id));
        });

        if (!course.getCourseName().equals(courseRequest.getCourseName())) {
            course.setCourseName(courseRequest.getCourseName());
        }
        if (!course.getImage().equals(courseRequest.getImage())) {
            course.setImage(courseRequest.getImage());
        }
        if (!course.getDescription().equals(courseRequest.getDescription())) {
            course.setDescription(courseRequest.getDescription());
        }
        if (!course.getDateOfGraduation().equals(courseRequest.getDateOfGraduation())) {
            course.setDateOfGraduation(courseRequest.getDateOfGraduation());
        }
        courseRepository.save(course);
        log.info("Курс успешно обновлен!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Курс с идентификатором: " + course.getCourseName() + " обновлен"))
                .build();
    }

    @Override
    public SimpleResponse deleteCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", id));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", id));
        });
        if (course.getGroup() == null) {
            courseRepository.delete(course);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Курс с идентификатором: " + id + " удален"))
                    .build();
        }
        course.getGroup().getCourses().remove(course);
        courseRepository.delete(course);
        log.info(String.format("Удалить ссылку по идентификатору: %s", id));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Курс с идентификатором: " + id + " удален"))
                .build();
    }

    @Override
    public List<InstructorResponseGroup> getAllInstructorsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", courseId));
        });

        List<InstructorResponseGroup> instructorResponseGroups = new ArrayList<>();
        for (Instructor instructor : course.getInstructors()) {
            InstructorResponseGroup instructorResponseGroup = new InstructorResponseGroup();
            instructorResponseGroup.setId(instructor.getId());
            instructorResponseGroup.setFullName(instructor.getUser().getFirstName().concat(" " + instructor.getUser().getLastName()));
            instructorResponseGroup.setSpecialization(instructor.getSpecialization());
            instructorResponseGroup.setPhoneNumber(instructor.getUser().getPhoneNumber());
            instructorResponseGroup.setEmail(instructor.getUser().getEmail());
            instructorResponseGroup.setPassword(instructor.getUser().getPassword());

            if (course.getGroup() != null) {
                instructorResponseGroup.setGroupId(course.getGroup().getId());
                instructorResponseGroup.setGroupName(course.getGroup().getGroupName());
            } else {
                instructorResponseGroup.setGroupId(null);
                instructorResponseGroup.setGroupName(null);
            }

            instructorResponseGroups.add(instructorResponseGroup);
        }

        log.info("Возврат {} групп ответов инструктора", instructorResponseGroups.size());
        return instructorResponseGroups;
    }

    @Override
    public List<StudentResponse> getAllStudentsByCourseId(Long courseId) {

        courseRepository.findById(courseId).orElseThrow(() ->
                new NotFoundException("Курс с идентификатором: " + courseId + " не найден"));

        log.info("Получение студентов для курса с идентификатором: {}", courseId);

        return studentRepository.getStudentByCourseId(courseId);
    }

    @Override
    public SimpleResponse addGroupToCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error(String.format("Группа с идентификатором: %s не найдена", groupId));
            return new NotFoundException(String.format("Группа с идентификатором: %s не найдена", groupId));
        });

        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", courseId));
        });

        if (course.getGroup() != null && course.getGroup().getId().equals(groupId)) {
            log.warn("Группа с идентификатором: {} уже связана с курсом ", groupId);
            throw new BadRequestException("Группа уже связана с курсом");
        }

        if (group.getCourses().contains(course)) {
            log.warn("Курс с идентификатором: {} уже связан с группой ", courseId);
            throw new BadRequestException("Курс уже связан с группой");
        }

        course.setGroup(group);
        groupRepository.save(group);
        log.info("Группа успешно сохранена!");
        group.getCourses().add(course);
        courseRepository.save(course);
        log.info("Курс успешно сохранен!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Группа успешно добавлена в курс")
                .build();
    }

    @Override
    public SimpleResponse deleteGroupToCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error(String.format("Группа с идентификатором: %s не найдена", groupId));
            return new NotFoundException(String.format("Группа с идентификатором: %s не найдена", groupId));
        });

        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найдена", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найдена", courseId));
        });

        if (!group.getCourses().contains(course)) {
            log.warn("Курс с идентификатором: {} не связан с группой с идентификатором: {}", courseId, groupId);
            throw new BadRequestException("Курс не связан с группой");
        }

        course.setGroup(null);
        group.getCourses().remove(course);

        groupRepository.save(group);
        log.info("Группа успешно обновлена!");

        courseRepository.save(course);
        log.info("Курс успешно обновлен!");

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Группа была успешно удалена с курса")
                .build();
    }

    @Override
    public SimpleResponse assignInstructorToCourse(Long courseId, AssigningInstructorsRequest request) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найдена", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найдена", courseId));
        });

        for (Long i : request.instructorsId()) {
            Instructor instructor = instructorRepository.findById(i).orElseThrow(() -> {
                log.error(String.format("Инструктор с идентификатором: %s не найден", i));
                return new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", i));
            });
            if (course.getInstructors().isEmpty()) {
                course.setInstructors(new ArrayList<>());
                course.getInstructors().add(instructor);
                courseRepository.save(course);
            } else {
                if (!course.getInstructors().contains(instructor)) {
                    course.getInstructors().add(instructor);
                    courseRepository.save(course);
                } else {
                    log.error(String.format("Инструктор с идентификатором: %s уже назначен", i));
                    throw new AlreadyExistException(String.format("Инструктор с идентификатором: %s уже назначен", i));
                }
            }
            if (instructor.getCourses().isEmpty()) {
                instructor.setCourses(new ArrayList<>());
                instructor.getCourses().add(course);
                instructorRepository.save(instructor);
            } else {
                instructor.getCourses().add(course);
                instructorRepository.save(instructor);
            }
        }
        log.info(String.format("Инструктор с идентификатором: %s назначен на курс с идентификатором:%s", request.instructorsId(), courseId));
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse deleteInstructorFromCourse(Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> {
            log.error(String.format("Инструктор с идентификатором: %s не найден", instructorId));
            return new NotFoundException(String.format("Инструктор с идентификатором: %s не найден", instructorId));
        });
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(String.format("Курс с идентификатором: %s не найден", courseId));
            return new NotFoundException(String.format("Курс с идентификатором: %s не найден", courseId));
        });
        if (course.getInstructors().contains(instructor)) {
            course.getInstructors().remove(instructor);
        } else {
            log.error(String.format("Инструктор с идентификатором: %s не включен в этот курс", instructorId));
            throw new BadRequestException(String.format("Инструктор с идентификатором: %s не включен в этот курс", instructorId));
        }

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public GroupResponseCourse getGroupByCourseId(Long courseId) {
        log.info("Удаление инструкторов из курса с идентификатором: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Курс с идентификатором: {} не найден", courseId);
                    return new NotFoundException("Курс с идентификатором: " + courseId + " не найдено");
                });

        if (course.getGroup() == null) {
            log.info("Курс с идентификатором {} не имеет связанной группы", courseId);
            return GroupResponseCourse.builder()
                    .id(null)
                    .groupName(null)
                    .build();
        }

        return GroupResponseCourse.builder()
                .id(course.getGroup().getId())
                .groupName(course.getGroup().getGroupName())
                .build();
    }


    @Override
    public SimpleResponse deleteInstructorsFromCourse(Long courseId) {
        log.info("Удаление инструкторов из курса с идентификатором: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Курс с идентификатором {} не найден", courseId);
                    return new NotFoundException("Курс с идентификатором: " + courseId + " не найдено");
                });

        for (Instructor instructor : course.getInstructors()) {
            instructor.getCourses().remove(course);
            instructorRepository.save(instructor);
            log.info("Инструктор с идентификатором {} удален из курса", instructor.getId());
        }

        course.getInstructors().clear();

        courseRepository.save(course);
        log.info("Инструкторы успешно удалены из курса");

        return new SimpleResponse(HttpStatus.OK, "Инструкторы успешно удалены из курса");
    }
}
