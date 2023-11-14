package com.app.lms.dto.linkDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class LinkResponse {

    private Long id;
    private String text;
    private String link;

    public LinkResponse(Long id, String text, String link) {
        this.id = id;
        this.text = text;
        this.link = link;
    }
}
