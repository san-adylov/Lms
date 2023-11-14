package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskAnswerDto.CheckRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerResponse;
import com.app.lms.entities.Student;
import com.app.lms.entities.Task;
import com.app.lms.entities.TaskAnswer;
import com.app.lms.entities.User;
import com.app.lms.enums.TaskAnswerStatus;
import com.app.lms.exceptions.BadCredentialException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.StudentRepository;
import com.app.lms.repositories.TaskAnswerRepository;
import com.app.lms.repositories.TaskRepository;
import com.app.lms.repositories.UserRepository;
import com.app.lms.service.TaskAnswerService;
import java.time.LocalDate;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TaskAnswerServiceImpl implements TaskAnswerService {

    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;
    private final TaskAnswerRepository taskAnswerRepository;
    private final UserRepository userRepository;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.getUserByEmail(name)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ником: %s не найден!", name)));
    }

    @Override
    public TaskAnswerResponse saveTaskAnswer(Long taskId, Long studentId, TaskAnswerRequest taskAnswerRequest) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskId));
            return new NotFoundException(String.format("Задание с идентификатором:%s не найдена", taskId));
        });
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            log.error(String.format("Студент с идентификатором: %s не найден", studentId));
            return new NotFoundException(String.format("Студент с идентификатором: %s не найден", studentId));
        });
        for (TaskAnswer a:task.getTaskAnswers()) {
            if (a.getStudent().equals(student)){
                log.info("Ответ на задачу успешно отправлено");
                return TaskAnswerResponse
                        .builder()
                        .taskAnswerStatus(null)
                        .isSend(false)
                        .build();
            }
        }
        TaskAnswer taskAnswer = new TaskAnswer();
        taskAnswer.setText(taskAnswerRequest.text());
        taskAnswer.setFile(taskAnswerRequest.file());
        taskAnswer.setComment(taskAnswerRequest.comment());
        if (LocalDate.now().isBefore(task.getDeadline())) {
            taskAnswer.setTaskAnswerStatus(TaskAnswerStatus.REVIEWED);
        }
        if (LocalDate.now().isAfter(task.getDeadline())) {
            taskAnswer.setTaskAnswerStatus(TaskAnswerStatus.REJECTED);
        }
        if (LocalDate.now().isEqual(task.getDeadline())){
            taskAnswer.setTaskAnswerStatus(TaskAnswerStatus.REVIEWED);
        }

        taskAnswerRepository.save(taskAnswer);
        taskAnswer.setTask(task);
        taskAnswer.setStudent(student);
        log.info("Успешно отправлено");

        return TaskAnswerResponse
                .builder()
                .taskAnswerStatus(taskAnswer.getTaskAnswerStatus())
                .isSend(true)
                .build();
    }

    @Override
    public SimpleResponse updateTaskAnswer(Long taskAnswerId, TaskAnswerRequest taskAnswerRequest) {
        TaskAnswer taskAnswer = taskAnswerRepository.findById(taskAnswerId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskAnswerId));
            return new NotFoundException(String.format("Задание с идентификатором: %s не найдена", taskAnswerId));
        });
        if (taskAnswer.getTaskAnswerStatus().equals(TaskAnswerStatus.REVIEWED)) {
            if (!taskAnswerRequest.text().equals(taskAnswer.getText())) {
                taskAnswer.setText(taskAnswerRequest.text());
            }
            if (!taskAnswerRequest.file().equals(taskAnswer.getFile())) {
                taskAnswer.setFile(taskAnswerRequest.file());
            }
            if (!taskAnswerRequest.comment().equals(taskAnswer.getComment())) {
                taskAnswer.setComment(taskAnswerRequest.comment());
            }
            taskAnswerRepository.save(taskAnswer);
            log.info("Успешно обновлено");
        } else throw new BadCredentialException("Вы не можете обновить задание!");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно обновлено")
                .build();
    }

    @Override
    public SimpleResponse checkTask(Long taskAnswerId, CheckRequest checkRequest) {
        TaskAnswer taskAnswer = taskAnswerRepository.findById(taskAnswerId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskAnswerId));
            return new NotFoundException(String.format("Задание с идентификатором: %s не найдена", taskAnswerId));
        });
        if (checkRequest.status().equals("accepted")) {
            taskAnswer.setTaskAnswerStatus(TaskAnswerStatus.ACCEPTED);
            log.info("Успешно принято!");
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Успешно принято!")
                    .build();
        }
        if (checkRequest.status().equals("rejected")) {
            taskAnswer.setTaskAnswerStatus(TaskAnswerStatus.REJECTED);
            log.info("Отклонено!");
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Отклонено!")
                    .build();
        }
        return null;
    }

    @Override
    public TaskAnswerResponse getTaskAnswer(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskId));
            return new NotFoundException(String.format("Задание с идентификатором: %s не найдена", taskId));
        });
        User user = getAuthentication();
        Student student = studentRepository.getStudentsByUserId(user.getId());
        TaskAnswer taskAnswer = taskAnswerRepository.getTaskAnswerByTaskId(task.getId(), student.getId());
        if (taskAnswer==null){
            log.info("Ответ на задачу еще не отправлен");
            return TaskAnswerResponse
                    .builder()
                    .taskAnswerStatus(null)
                    .isSend(false)
                    .build();
        }
        return TaskAnswerResponse.builder()
                .taskAnswerId(taskAnswer.getId())
                .comment(taskAnswer.getComment())
                .file(taskAnswer.getFile())
                .taskAnswerStatus(taskAnswer.getTaskAnswerStatus())
                .text(taskAnswer.getText())
                .isSend(true)
                .build();
    }
}
