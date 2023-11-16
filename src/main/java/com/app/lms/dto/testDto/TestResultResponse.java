package com.app.lms.dto.testDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TestResultResponse {

    private boolean isAccepted;
    private int quantityOfTestAnswer;
    List<TestResultInstructorResponse> testResultsListOFStudents;
}
