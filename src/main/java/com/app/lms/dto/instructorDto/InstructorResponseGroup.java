package com.app.lms.dto.instructorDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class InstructorResponseGroup {
    private Long id;
    private String fullName;
    private String specialization;
    private String phoneNumber;
    private String email;
    private String password;
   private Long groupId;
   private String groupName;

    public InstructorResponseGroup(Long id, String fullName, String specialization, String phoneNumber, String email, String password, Long groupId, String groupName) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
