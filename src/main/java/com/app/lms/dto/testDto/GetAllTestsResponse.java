package com.app.lms.dto.testDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllTestsResponse {

    private Long testId;
    private String testName;

    public GetAllTestsResponse(Long testId, String testName) {
        this.testId = testId;
        this.testName = testName;
    }
}
