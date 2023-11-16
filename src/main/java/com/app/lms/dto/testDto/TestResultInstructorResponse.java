package com.app.lms.dto.testDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResultInstructorResponse {

    private String fullName;
    private int correctAnswers;
    private int inCorrectAnswers;
    private int point;

    public TestResultInstructorResponse(String fullName, int correctAnswers, int inCorrectAnswers) {
        this.fullName = fullName;
        this.correctAnswers = correctAnswers;
        this.inCorrectAnswers = inCorrectAnswers;
    }
}
