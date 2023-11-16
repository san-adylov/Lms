package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskDto.GetTaskByIdResponse;
import com.app.lms.dto.taskDto.TaskGetAllResponse;
import com.app.lms.dto.taskDto.TaskRequest;
import com.app.lms.entities.Lesson;
import com.app.lms.entities.Task;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.LessonRepository;
import com.app.lms.repositories.TaskRepository;
import com.app.lms.service.TaskService;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<TaskGetAllResponse> getAllTasks(Long lessonId) {
        return taskRepository.getAllTasks(lessonId);
    }

    @Override
    public SimpleResponse saveTask(Long lessonId, TaskRequest taskRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
            return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
        });

        Task task = new Task();
        task.setTaskName(taskRequest.TaskName());
        task.setText(taskRequest.text());
        task.setFileName(taskRequest.fileName());
        task.setFileLink(taskRequest.fileLink());
        task.setLinkName(taskRequest.linkName());
        task.setLink(taskRequest.link());
        task.setImage(taskRequest.image());
        task.setCode(taskRequest.code());
        task.setDeadline(taskRequest.deadLine());
        taskRepository.save(task);
        task.setLesson(lesson);
        log.info("Задание сохранена");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskId));
            return new NotFoundException(String.format("Задание с идентификатором: %s не найдена", taskId));
        });
        if (!task.getTaskName().equals(taskRequest.TaskName())){
            task.setTaskName(taskRequest.TaskName());
        }
        if (!task.getText().equals(taskRequest.text())){
            task.setText(taskRequest.text());
        }
        if (!task.getFileName().equals(taskRequest.fileName())){
            task.setFileName(taskRequest.fileName());
        }
        if (!task.getFileLink().equals(taskRequest.fileLink())){
            task.setFileLink(taskRequest.fileLink());
        }
        if (!task.getLinkName().equals(taskRequest.linkName())){
            task.setLinkName(taskRequest.linkName());
        }
        if (!task.getLink().equals(taskRequest.link())){
            task.setLink(taskRequest.link());
        }
        if (!task.getImage().equals(taskRequest.image())){
            task.setImage(taskRequest.image());
        }
        if (!task.getCode().equals(taskRequest.code())){
            task.setCode(taskRequest.code());
        }
        taskRepository.save(task);
        log.info("Task successfully updated");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public GetTaskByIdResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskId));
            return new NotFoundException(String.format("Задание с идентификатором:%s не найдена", taskId));
        });
        log.info(String.format("Получить задание по идентификатору: %s ", taskId));
        return GetTaskByIdResponse.builder()
                .id(task.getId())
                .taskName(task.getTaskName())
                .text(task.getText())
                .fileName(task.getFileName())
                .fileLink(task.getFileLink())
                .linkName(task.getLinkName())
                .link(task.getLink())
                .image(task.getImage())
                .code(task.getCode())
                .deadline(task.getDeadline())
                .build();
    }

    @Override
    public SimpleResponse deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error(String.format("Задание с идентификатором: %s не найдена", taskId));
            return new NotFoundException(String.format("Задание с идентификатором:%s не найдена", taskId));
        });
        task.getLesson().getTasks().remove(task);
        taskRepository.deleteById(taskId);
        log.info(String.format("Удалить задание по идентификатору: %s ", taskId));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }
}