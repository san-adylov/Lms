package com.app.lms.dto.taskAnswerDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.app.lms.enums.TaskAnswerStatus;

@Getter
@Setter
@Builder
public class TaskAnswerResponse {

    private Long taskAnswerId;
    private String comment;
    private String file;
    private String text;
    private TaskAnswerStatus taskAnswerStatus;
    private boolean isSend;
}