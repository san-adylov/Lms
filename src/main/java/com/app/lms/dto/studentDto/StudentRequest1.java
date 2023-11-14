package com.app.lms.dto.studentDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import com.app.lms.enums.StudyFormat;

@Builder
public record StudentRequest1(@NotNull
                              String firstName,
                              @NotNull
                              String lastName,
                              @NotNull
                              String email,
                              @NotNull
                              String phoneNumber,
                              @NotNull
                              StudyFormat studyFormat) {
}
