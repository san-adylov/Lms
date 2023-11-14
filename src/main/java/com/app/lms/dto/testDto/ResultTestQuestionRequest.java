package com.app.lms.dto.testDto;

import java.util.List;

public record ResultTestQuestionRequest(Long questionId,
                                        List<Long> optionId) {
}
