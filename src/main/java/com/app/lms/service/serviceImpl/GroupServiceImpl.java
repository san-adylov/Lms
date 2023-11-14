package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.groupDto.GroupRequest;
import com.app.lms.dto.groupDto.GroupResponse;
import com.app.lms.dto.studentDto.StudentResponseGroup;
import com.app.lms.entities.Course;
import com.app.lms.entities.Group;
import com.app.lms.entities.Student;
import com.app.lms.exceptions.AlreadyExistException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.CourseRepository;
import com.app.lms.repositories.GroupRepository;
import com.app.lms.repositories.StudentRepository;
import com.app.lms.service.GroupService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    @Override
    public SimpleResponse saveGroup(GroupRequest groupRequest) {
        boolean groupExists = groupRepository.existsByGroupName(groupRequest.getGroupName());
        if (groupExists) {
            log.info("Группа с названием: {} уже существует", groupRequest.getGroupName());
            throw new AlreadyExistException("Название группы уже существует!");
        }
        Group group = new Group();
        group.setGroupName(groupRequest.getGroupName());
        group.setImage(groupRequest.getImage());
        group.setDescription(groupRequest.getDescription());
        group.setCreateDate(LocalDate.now());
        group.setDateOfGraduate(groupRequest.getDateOfGraduation());
        groupRepository.save(group);
        log.info("Новая группа успешно сохранена!");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Группа успешно сохранена")
                .build();
    }

    @Override
    public List<GroupResponse> getAllGroups() {
        log.info("Список успешно полученных групп");
       return groupRepository.getAllGroups();
    }

    @Override
    public SimpleResponse updateGroup(Long groupId, GroupRequest groupRequest) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> {
                    log.error("Группа с идентификатором {} не найдена", groupId);
                    return new NotFoundException("Группа не найдена!");
                });
        if (!groupRequest.getGroupName().equalsIgnoreCase(group.getGroupName())) {
            if (groupRepository.existsByGroupName(groupRequest.getGroupName())) {
                log.info("Группа с названием: {} уже существует", groupRequest.getGroupName());
                throw new AlreadyExistException("Название группы уже существует!");
            }
            group.setGroupName(groupRequest.getGroupName());
        }
        if (!groupRequest.getImage().equals(group.getImage())) {
            group.setImage(groupRequest.getImage());
        }
        if (!groupRequest.getDescription().equals(group.getDescription())) {
            group.setDescription(groupRequest.getDescription());
        }
        if (!groupRequest.getDateOfGraduation().equals(group.getDateOfGraduate())) {
            group.setDateOfGraduate(groupRequest.getDateOfGraduation());
        }
        log.info("Группа успешно обновлена!");
        groupRepository.save(group);
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно обновлено!")
                .build();
    }

    @Override
    public List<StudentResponseGroup> getGroupById(Long groupId) {
        log.info("Получение учащихся в группу с идентификатором: {}", groupId);
        List<Student> students = studentRepository.findByGroupId(groupId);
        List<StudentResponseGroup> studentResponses = new ArrayList<>();

        for (Student student : students) {
            StudentResponseGroup studentResponse = new StudentResponseGroup();
            studentResponse.setId(student.getId());
            studentResponse.setFullName(student.getUser().getFirstName().concat(" ").concat(student.getUser().getLastName()));
            studentResponse.setStudyFormat(student.getStudyFormat());
            studentResponse.setPhoneNumber(student.getUser().getPhoneNumber());
            studentResponse.setEmail(student.getUser().getEmail());
            studentResponse.setGroup(student.getGroup().getGroupName());
            studentResponses.add(studentResponse);
        }
        log.info("Найдено {} студентов для группы с идентификатором: {}", studentResponses.size(), groupId);
        return studentResponses;
    }

    @Override
    public SimpleResponse deleteGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> {
                    log.error("Группа с идентификатором {} не найдена", groupId);
                    return new NotFoundException("Группа не найдена!");
                });
        if (group.getCourses() == null) {
            groupRepository.deleteById(groupId);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Группа с идентификатором: " + groupId + " было успешно удалено"))
                    .build();
        }

        for (Course course : group.getCourses()) {
            course.setGroup(null);
            courseRepository.save(course);
        }
        groupRepository.deleteById(groupId);
        log.info("Удаление группы по идентификатору {} выполнено успешно", groupId);
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Удалить группу..")
                .build();
    }
}
