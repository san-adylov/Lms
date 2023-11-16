package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.videoLessonDto.VideoLessonRequest;
import com.app.lms.dto.videoLessonDto.VideoLessonResponse;

import java.util.List;

public interface VideoLessonService {

    SimpleResponse saveVideoLesson(Long lessonId, VideoLessonRequest videoLessonRequest);

    List<VideoLessonResponse> getAllVideoLesson(Long lessonId);

    VideoLessonResponse getVideoLessonById(Long videoLessonId);

    SimpleResponse updateVideoLesson(Long videoLessonId, VideoLessonRequest videoLessonRequest);

    SimpleResponse deleteVideoLessonById(Long videoLessonId);
}
