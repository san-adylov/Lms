package com.app.lms.dto.groupDto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class GroupResponse {
    private Long id;
    private String groupName;
    private String image;
    private String description;
    private LocalDate dateOfGraduate;

    public GroupResponse(Long id, String groupName, String image, String description, LocalDate dateOfGraduate) {
        this.id = id;
        this.groupName = groupName;
        this.image = image;
        this.description = description;
        this.dateOfGraduate = dateOfGraduate;
    }
}
