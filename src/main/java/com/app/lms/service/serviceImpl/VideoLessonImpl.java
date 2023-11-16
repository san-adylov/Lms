package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.videoLessonDto.VideoLessonRequest;
import com.app.lms.dto.videoLessonDto.VideoLessonResponse;
import com.app.lms.entities.Lesson;
import com.app.lms.entities.VideoLesson;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.repositories.LessonRepository;
import com.app.lms.repositories.VideoLessonRepository;
import com.app.lms.service.VideoLessonService;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class VideoLessonImpl implements VideoLessonService {

    private final VideoLessonRepository videoLessonRepository;
    private final LessonRepository lessonRepository;

    @Override
    public SimpleResponse saveVideoLesson(Long lessonId, VideoLessonRequest videoLessonRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> {
                    log.error("Урок с идентификатором {} не найден", lessonId);
                    return new NotFoundException("Урок не найден!");
                });

        if(!lesson.getVideoLessons().isEmpty()){
            throw new BadRequestException("Вы можете добавить только одно значение");
        }
        VideoLesson videoLesson = new VideoLesson();
        videoLesson.setName(videoLessonRequest.name());
        videoLesson.setDescription(videoLessonRequest.description());
        videoLesson.setLink(videoLessonRequest.link());
        videoLesson.setLesson(lesson);
        videoLessonRepository.save(videoLesson);
        log.info("Видеоурок успешно сохранен");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно сохранено")
                .build();
    }

    @Override
    public List<VideoLessonResponse> getAllVideoLesson(Long lessonId) {
        log.info("Список успешно полученных видеоуроков");
        return videoLessonRepository.getAllVideoLesson(lessonId);
    }

    @Override
    public VideoLessonResponse getVideoLessonById(Long videoLessonId) {
        return videoLessonRepository.getVideoLessonById(videoLessonId).orElseThrow(
                () -> {
                    log.error(String.format("Видеоурок с идентификатором: %s не найден", videoLessonId));
                    return new NotFoundException(String.format("Видеоурок с идентификатором: %s не найден!", videoLessonId));
                });
    }

    @Override
    public SimpleResponse updateVideoLesson(Long videoLessonId, VideoLessonRequest videoLessonRequest) {
        VideoLesson videoLesson = videoLessonRepository.findById(videoLessonId).orElseThrow(
                () -> {
                    log.error(String.format("Видеоурок с идентификатором: %s не найден ", videoLessonId));
                    return new NotFoundException("Видеоурок не найден!");
                });

        if (!videoLessonRequest.name().equals(videoLesson.getName())) {
            videoLesson.setName(videoLessonRequest.name());
        }
        if (!videoLessonRequest.description().equals(videoLesson.getDescription())) {
            videoLesson.setDescription(videoLessonRequest.description());
        }
        if (!videoLessonRequest.link().equals(videoLesson.getLink())) {
            videoLesson.setLink(videoLessonRequest.link());
        }
        videoLessonRepository.save(videoLesson);
        log.info("Видеоурок успешно обновлен");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно обновлено")
                .build();
    }

    @Override
    public SimpleResponse deleteVideoLessonById(Long videoLessonId) {
        VideoLesson videoLesson = videoLessonRepository.findById(videoLessonId).orElseThrow(
                () -> {
                    log.error(String.format("Видеоурок с идентификатором: %s не найден ", videoLessonId));
                    return new NotFoundException(String.format("Видеоурок с идентификатором: %s не найден ", videoLessonId));
                });
        if (videoLesson.getLesson() == null) {
            videoLessonRepository.delete(videoLesson);
            return SimpleResponse
                    .builder()
                    .status(HttpStatus.OK)
                    .message("Успешно удалено")
                    .build();
        }
        videoLesson.getLesson().getVideoLessons().remove(videoLesson);
        videoLessonRepository.delete(videoLesson);
        log.info("Видеоурок успешно удален");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }
}
