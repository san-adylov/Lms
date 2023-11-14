package com.app.lms.service;

import java.util.List;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.lessonDto.LessonRequest;
import com.app.lms.dto.lessonDto.LessonResponse;

public interface LessonService {

      List<LessonResponse> getAllLessons(Long courseId);

      SimpleResponse saveLesson(Long courseId, LessonRequest lessonRequest);

      SimpleResponse deleteLessonById(Long lessonId);

      SimpleResponse updateLesson(Long lessonId,LessonRequest lessonRequest);
}
