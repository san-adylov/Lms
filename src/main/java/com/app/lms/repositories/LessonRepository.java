package com.app.lms.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.lessonDto.LessonResponse;
import com.app.lms.entities.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
  
  @Query("select new com.app.lms.dto.lessonDto.LessonResponse(l.id,l.lessonName) from Lesson l where l.course.id = :id order by l.id desc ")
  List<LessonResponse> getAllLessons(@Param("id") Long courseId);
}