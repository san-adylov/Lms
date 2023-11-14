package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.lessonDto.LessonRequest;
import com.app.lms.dto.lessonDto.LessonResponse;
import com.app.lms.service.LessonService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lessons")
@CrossOrigin(value = "*", maxAge = 3600)
@Tag(name = "Lesson API", description = "API for lesson CRUD management")
public class LessonApi {

  private final LessonService lessonService;

  @GetMapping("/{courseId}")
  @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
  @Operation(summary = "Get all lessons", description = "Get all instructor by course id")
  public List<LessonResponse> getAllLessons(@PathVariable Long courseId) {
    return lessonService.getAllLessons(courseId);
  }

  @PostMapping("/{courseId}")
  @PreAuthorize("hasAuthority('INSTRUCTOR')")
  @Operation(summary = "Save lesson", description = "Save lesson by course id")
  public SimpleResponse saveLesson(@PathVariable Long courseId, @RequestBody LessonRequest lessonRequest) {
    return lessonService.saveLesson(courseId, lessonRequest);
  }

  @DeleteMapping("/{lessonId}")
  @PreAuthorize("hasAuthority('INSTRUCTOR')")
  @Operation(summary = "Delete lesson", description = "Delete lesson by id")
  public SimpleResponse deleteLesson(@PathVariable Long lessonId) {
    return lessonService.deleteLessonById(lessonId);
  }

  @PutMapping("/{lessonId}")
  @PreAuthorize("hasAuthority('INSTRUCTOR')")
  @Operation(summary = "Update lesson", description = "Update lesson by id")
  public SimpleResponse updateLesson(@PathVariable Long lessonId, @RequestBody LessonRequest lessonRequest) {
    return lessonService.updateLesson(lessonId, lessonRequest);
  }
}
