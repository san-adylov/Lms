package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.lessonDto.LessonRequest;
import com.app.lms.dto.lessonDto.LessonResponse;
import com.app.lms.entities.Course;
import com.app.lms.entities.Lesson;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.CourseRepository;
import com.app.lms.repositories.LessonRepository;
import com.app.lms.service.LessonService;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements LessonService {

  private final LessonRepository lessonRepository;
  private final CourseRepository courseRepository;

  @Override
  public List<LessonResponse> getAllLessons(Long courseId) {
    log.info("Получить все уроки");
    List<LessonResponse> allLessons = lessonRepository.getAllLessons(courseId);
    for (LessonResponse l:allLessons) {
      Lesson lesson = lessonRepository.findById(l.getLessonId()).orElseThrow(
              () -> {log.error(String.format("Урок с идентификатором: %s не найден", l.getLessonId()));
                return new NotFoundException(String.format("Урок с идентификатором: %s не найден", l.getLessonId()));});
      if (!lesson.getVideoLessons().isEmpty()){
        l.setVideoLesson(true);
      }
      if (!lesson.getPresentations().isEmpty()){
        l.setPresentation(true);
      }
      if (!lesson.getTasks().isEmpty()){
        l.setTask(true);
      }
      if (!lesson.getLinks().isEmpty()){
        l.setLink(true);
      }
      if (!lesson.getTests().isEmpty()){
        l.setTest(true);
      }
    }
    return allLessons;
  }

  @Override
  public SimpleResponse saveLesson(Long courseId, LessonRequest lessonRequest) {

    Course course = courseRepository.findById(courseId).orElseThrow(
        () -> {log.error(String.format("Курс с идентификатором: %s не найден", courseId));
        return new NotFoundException(String.format("Курс с идентификатором: %s не найден", courseId));});
    Lesson lesson = new Lesson();
    lesson.setLessonName(lessonRequest.lessonName());
    lessonRepository.save(lesson);
    lesson.setCourse(course);
    log.info("Урок успешно сохранен");
    return SimpleResponse.builder()
        .status(HttpStatus.OK)
        .message("Успешно")
        .build();
  }

  @Override
  public SimpleResponse deleteLessonById(Long lessonId) {

    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
        () -> {log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
        return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));});
    lesson.getCourse().getLessons().remove(lesson);
    lessonRepository.delete(lesson);
    log.info("Урок успешно удален");
    return SimpleResponse.builder()
        .status(HttpStatus.OK)
        .message("Успешно")
        .build();

  }

  @Override
  public SimpleResponse updateLesson(Long lessonId, LessonRequest lessonRequest) {

    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
        () ->{log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
        return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));});
    lesson.setLessonName(lessonRequest.lessonName());
    lessonRepository.save(lesson);
    log.info("Урок успешно обновлен");
    return SimpleResponse.builder()
        .status(HttpStatus.OK)
        .message("Успешно")
        .build();
  }
}
