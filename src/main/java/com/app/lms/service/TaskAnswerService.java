package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.taskAnswerDto.CheckRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerRequest;
import com.app.lms.dto.taskAnswerDto.TaskAnswerResponse;

public interface TaskAnswerService {

    TaskAnswerResponse saveTaskAnswer(Long taskId, Long studentId, TaskAnswerRequest taskAnswerRequest);

    SimpleResponse updateTaskAnswer(Long taskAnswerId, TaskAnswerRequest taskAnswerRequest);

    SimpleResponse checkTask(Long taskAnswerId, CheckRequest checkRequest);

    TaskAnswerResponse getTaskAnswer(Long taskId);
}
