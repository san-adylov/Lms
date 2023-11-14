package com.app.lms.dto.studentDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import com.app.lms.enums.StudyFormat;
import com.app.lms.validation.email.EmailValid;

@Builder
public record StudentRequest(
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @NotNull
        @EmailValid
        String email,
        @NotNull
        String phoneNumber,
        @NotNull
        StudyFormat studyFormat
) {
}
