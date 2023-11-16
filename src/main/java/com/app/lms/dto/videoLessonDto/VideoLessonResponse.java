package com.app.lms.dto.videoLessonDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class VideoLessonResponse {
    private Long id;
    private String name;
    private String description;
    private String link;

    public VideoLessonResponse(Long id, String name, String description, String link) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
    }
}
