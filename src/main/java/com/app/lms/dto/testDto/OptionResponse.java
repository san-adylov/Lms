package com.app.lms.dto.testDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OptionResponse {

    private Long optionId;
    private String option;
    private boolean isTrue;
    private boolean isChosen;

    public OptionResponse(String option) {
        this.option = option;
    }

    public OptionResponse(Long optionId, String option) {
        this.optionId = optionId;
        this.option = option;
    }


    public OptionResponse(Long optionId, String option, boolean isTrue) {
        this.optionId = optionId;
        this.option = option;
        this.isTrue = isTrue;
    }

    public OptionResponse(Long optionId, String option, boolean isTrue, boolean isChosen) {
        this.optionId = optionId;
        this.option = option;
        this.isTrue = isTrue;
        this.isChosen = isChosen;
    }
}
