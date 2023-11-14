package com.app.lms.dto.presentationDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class PresentationResponse {
    private Long id;
    private String name;
    private String description;
    private String linkFilePpt;

    public PresentationResponse(Long id, String name, String description, String linkFilePpt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.linkFilePpt = linkFilePpt;
    }
}
