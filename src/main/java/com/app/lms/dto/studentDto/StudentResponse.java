package com.app.lms.dto.studentDto;

import lombok.*;
import com.app.lms.enums.StudyFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class StudentResponse {

    private Long id;
    private String fullName;
    private String groupName;
    private String email;
    private String password;
    private String phoneNumber;
    private StudyFormat studyFormat;

    public StudentResponse(Long id, String fullName, String groupName, String email, String password, String phoneNumber, StudyFormat studyFormat) {
        this.id = id;
        this.fullName = fullName;
        this.groupName = groupName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.studyFormat = studyFormat;
    }

    public StudentResponse(long l, String s) {

    }
}
