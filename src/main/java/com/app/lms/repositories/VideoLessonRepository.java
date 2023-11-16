package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.videoLessonDto.VideoLessonResponse;
import com.app.lms.entities.VideoLesson;

import java.util.List;
import java.util.Optional;

public interface VideoLessonRepository extends JpaRepository<VideoLesson, Long> {

    @Query("select new com.app.lms.dto.videoLessonDto.VideoLessonResponse(v.id,v.name,v.description,v.link)from VideoLesson v join v.lesson l where l.id= :lessonId")
    List<VideoLessonResponse> getAllVideoLesson(@Param("lessonId") Long lessonId);

    @Query("select new com.app.lms.dto.videoLessonDto.VideoLessonResponse(v.id,v.name,v.description,v.link)from VideoLesson v where v.id= :videoLessonId")
    Optional<VideoLessonResponse> getVideoLessonById(@Param("videoLessonId") Long videoLessonId);
}