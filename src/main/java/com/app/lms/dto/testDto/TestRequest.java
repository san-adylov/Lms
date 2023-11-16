package com.app.lms.dto.testDto;

import java.util.List;

public record TestRequest(String testName,
                          List<QuestionRequest> questionRequests) {
    public TestRequest {
    }
}
