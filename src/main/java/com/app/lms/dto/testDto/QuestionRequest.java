package com.app.lms.dto.testDto;

import com.app.lms.enums.QuestionType;

import java.util.List;

public record QuestionRequest(Long questionId,
                              String question,
                              QuestionType questionType,
                              List<OptionRequest> optionRequests) {
}
