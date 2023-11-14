package com.app.lms.dto.studentDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.lms.enums.StudyFormat;

@Getter
@Setter
@NoArgsConstructor
public class StudentResponseGroup {
    private Long id;
    private String fullName;
    private StudyFormat studyFormat;
    private String phoneNumber;
    private String email;
    private String group;

    public StudentResponseGroup(Long id, String fullName, StudyFormat studyFormat, String phoneNumber, String email, String group) {
        this.id = id;
        this.fullName = fullName;
        this.studyFormat = studyFormat;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.group = group;
    }
}
