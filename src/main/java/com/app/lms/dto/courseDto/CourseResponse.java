package com.app.lms.dto.courseDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String courseName;
    private String image;
    private String description;
    private LocalDate dateOfGraduation;
    private Long groupId;
    private String groupName;

    public CourseResponse(Long id, String courseName, String image, String description, LocalDate dateOfGraduation, Long groupId, String groupName) {
        this.id = id;
        this.courseName = courseName;
        this.image = image;
        this.description = description;
        this.dateOfGraduation = dateOfGraduation;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public CourseResponse(Long id, String courseName, String image, String description, LocalDate dateOfGraduation) {
        this.id = id;
        this.courseName = courseName;
        this.image = image;
        this.description = description;
        this.dateOfGraduation = dateOfGraduation;
    }
}
