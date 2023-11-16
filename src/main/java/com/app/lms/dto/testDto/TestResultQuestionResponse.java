package com.app.lms.dto.testDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.lms.enums.QuestionType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TestResultQuestionResponse {

    private Long id;
    private String question;
    private QuestionType questionType;
    private double point;
    private Long questionId;
    List<OptionResponse> optionResponses;

    public TestResultQuestionResponse(Long id, String question, double point,Long questionId) {
        this.id = id;
        this.question = question;
        this.point = point;
        this.questionId = questionId;
    }

    public TestResultQuestionResponse(Long id, String question, double point, Long questionId, QuestionType questionType) {
        this.id = id;
        this.question = question;
        this.point = point;
        this.questionId = questionId;
        this.questionType = questionType;
    }
}
