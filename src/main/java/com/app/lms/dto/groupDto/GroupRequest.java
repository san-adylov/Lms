package com.app.lms.dto.groupDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {
    @NotBlank(message = "Group name cannot be empty")
    private String groupName;
    @NotBlank(message = "Image cannot be empty")
    private String image;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @NotNull(message = "Date of graduation cannot be null")
    private LocalDate dateOfGraduation;


}
