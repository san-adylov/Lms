package com.app.lms.dto.testDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TestResponse {

    private String testName;
    private boolean isPassed;
    private List<QuestionResponse> questionResponses;
}
