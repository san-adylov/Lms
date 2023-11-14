package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskAnswerDto.CheckRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerResponse;
import com.app.lms.service.TaskAnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/taskAnswers")
@CrossOrigin(value = "*", maxAge = 3600)
@Tag(name = "Task answer API", description = "API for task answer management")
public class TaskAnswerApi {

    private final TaskAnswerService taskAnswerService;

    @PostMapping("/save/{taskId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Save task answer", description = "Save task answer by task id,student id and check deadline")
    public TaskAnswerResponse saveTaskAnswer(@PathVariable Long taskId,
                                             @RequestParam Long studentId,
                                             @RequestBody TaskAnswerRequest taskAnswerRequest) {
        return taskAnswerService.saveTaskAnswer(taskId, studentId, taskAnswerRequest);
    }

    @PutMapping("/{taskAnswerId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Update task answer", description = "Update task answer by id")
    public SimpleResponse updateTaskAnswer(@PathVariable Long taskAnswerId,
                                           @RequestBody TaskAnswerRequest taskAnswerRequest) {
        return taskAnswerService.updateTaskAnswer(taskAnswerId, taskAnswerRequest);
    }

    @PostMapping("/{taskAnswerId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Check task answer", description = "Change task answer status")
    public SimpleResponse checkTask(@PathVariable Long taskAnswerId,
                                    @RequestBody CheckRequest checkRequest) {
        return taskAnswerService.checkTask(taskAnswerId, checkRequest);
    }
    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get task answer", description = "Get task answer by task id")
    public TaskAnswerResponse getTaskAnswer(@PathVariable Long taskId){
        return taskAnswerService.getTaskAnswer(taskId);
    }
}
