package com.app.lms.dto.taskDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TaskGetAllResponse {

    private Long taskId;
    private String taskName;

    public TaskGetAllResponse(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}