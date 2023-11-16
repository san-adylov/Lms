package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskDto.GetTaskByIdResponse;
import com.app.lms.dto.taskDto.TaskGetAllResponse;
import com.app.lms.dto.taskDto.TaskRequest;

import java.util.List;

public interface TaskService {

    List<TaskGetAllResponse> getAllTasks(Long lessonId);

    SimpleResponse saveTask(Long lessonId, TaskRequest taskRequest);

    SimpleResponse updateTask(Long taskId, TaskRequest taskRequest);

    GetTaskByIdResponse getTaskById(Long taskId);

    SimpleResponse deleteTask(Long taskId);
}