package com.app.lms.dto.testDto;

import lombok.Getter;
import lombok.Setter;
import com.app.lms.enums.QuestionType;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {

    private Long id;
    private String question;
    private QuestionType questionType;
    private List<OptionResponse> optionResponses;

    public QuestionResponse(Long id, String question, QuestionType questionType) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
    }
}
