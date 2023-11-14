package com.app.lms.dto.lessonDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LessonResponse {

  private Long lessonId;
  private String lessonName;
  private boolean isVideoLesson;
  private boolean isPresentation;
  private boolean isTask;
  private boolean isLink;
  private boolean isTest;

  public LessonResponse(Long lessonId, String lessonName) {
    this.lessonId = lessonId;
    this.lessonName = lessonName;
  }
}
