package com.app.lms.dto.instructorDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class InstructorResponse {

    private Long id;
    private String fullName;
    private String specialization;
    private String phoneNumber;
    private String email;
    private String password;

    public InstructorResponse(Long id, String fullName, String specialization, String phoneNumber, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
}
