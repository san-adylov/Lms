package com.app.lms.dto.courseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    @NotNull
    @NotBlank
    private String courseName;
    @NotNull
    @NotBlank
    private String image;
    @NotNull
    @NotBlank
    private String description;
    @NotNull(message = "Date of graduation should not be null")
    private LocalDate dateOfGraduation;
}

