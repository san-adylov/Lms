package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskDto.GetTaskByIdResponse;
import com.app.lms.dto.taskDto.TaskGetAllResponse;
import com.app.lms.dto.taskDto.TaskRequest;
import com.app.lms.service.TaskService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@CrossOrigin(value = "*",maxAge = 3600)
@Tag(name = "Task API", description = "API for task CRUD management")
public class TaskApi {

    private final TaskService taskService;

    @GetMapping("/{lessonId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get all tasks", description = "Get all tasks by lesson id")
    public List<TaskGetAllResponse> getAllTasks(@PathVariable Long lessonId){
        return taskService.getAllTasks(lessonId);
    }

    @PostMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Save task",description = "Save task by lesson id")
    public SimpleResponse saveTask(@PathVariable Long lessonId, @RequestBody TaskRequest taskRequest){
        return taskService.saveTask(lessonId, taskRequest);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "update task",description = "Update task by lesson id")
    public SimpleResponse updateTask(@PathVariable Long taskId, @RequestBody TaskRequest taskRequest){
        return taskService.updateTask(taskId, taskRequest);
    }

    @GetMapping("/getById/{taskId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get task",description = "Get task by id")
    public GetTaskByIdResponse getTaskById(@PathVariable Long taskId){
        return taskService.getTaskById(taskId);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Delete task", description = "Delete task by id")
    public SimpleResponse deleteTask(@PathVariable Long taskId){
        return taskService.deleteTask(taskId);
    }
}