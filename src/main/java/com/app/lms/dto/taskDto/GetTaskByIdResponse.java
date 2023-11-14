package com.app.lms.dto.taskDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class GetTaskByIdResponse {

    private Long id;
    private String taskName;
    private String text;
    private String fileName;
    private String fileLink;
    private String linkName;
    private String link;
    private String image;
    private String code;
    private LocalDate deadline;
}