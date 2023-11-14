package com.app.lms.dto.taskDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {

    private String name;
    private String text;

    public TaskResponse(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
