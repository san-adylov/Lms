package com.app.lms.dto.studentDto;

import lombok.Builder;
import java.util.List;

@Builder
public record PaginationStudentResponse(
        List<StudentResponse>studentResponses,
        int size,
        int page,
        int quantityOfStudents
) {
}