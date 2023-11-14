package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.presentationDto.PresentationRequest;
import com.app.lms.dto.presentationDto.PresentationResponse;
import com.app.lms.entities.Lesson;
import com.app.lms.entities.Presentation;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.LessonRepository;
import com.app.lms.repositories.PresentationRepository;
import com.app.lms.service.PresentationService;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class PresentationServiceImpl implements PresentationService {

    private final LessonRepository lessonRepository;
    private final PresentationRepository presentationRepository;

    @Override
    public SimpleResponse savePresentation(Long lessonId, PresentationRequest presentationRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
            return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
        });

        if (!lesson.getPresentations().isEmpty()) {
            throw new  BadRequestException("Вы можете добавить только одно значение");
        }
        Presentation presentation = new Presentation();
        presentation.setName(presentationRequest.name());
        presentation.setDescription(presentationRequest.description());
        presentation.setLinkFilePpt(presentationRequest.linkPptFile());
        presentation.setLesson(lesson);
        presentationRepository.save(presentation);
        log.info("Презентация успешно сохранена");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно сохранено")
                .build();
    }

    @Override
    public PresentationResponse getByIdPresentation(Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId).orElseThrow(() -> {
            log.error(String.format("Презентация с идентификатором: %s не найдена", presentationId));
            return new NotFoundException(String.format("Презентация с идентификатором:%s не найдена", presentationId));
        });
        log.info(String.format("Получить презентацию с идентификатором: %s", presentationId));

        return PresentationResponse
                .builder()
                .name(presentation.getName())
                .description(presentation.getDescription())
                .linkFilePpt(presentation.getLinkFilePpt())
                .build();
    }

    @Override
    public SimpleResponse updatePresentation(Long presentationId, PresentationRequest presentationRequest) {
        Presentation presentation = presentationRepository.findById(presentationId).orElseThrow(() -> {
            log.error(String.format("Презентация с идентификатором: %s не найдена", presentationId));
            return new NotFoundException(String.format("Презентация с идентификатором: %s не найдена", presentationId));
        });
        presentation.setName(presentationRequest.name());
        presentation.setDescription(presentationRequest.description());
        presentation.setLinkFilePpt(presentationRequest.linkPptFile());
        presentationRepository.save(presentation);
        log.info("Презентация успешно обновлена");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно обновлено")
                .build();
    }

    @Override
    public SimpleResponse deletePresentation(Long presentationId) {
        presentationRepository.findById(presentationId).orElseThrow(() -> {
            log.error(String.format("PПрезентация с идентификатором: %s не найдена", presentationId));
            return new NotFoundException(String.format("Презентация с идентификатором: %s не найдена", presentationId));
        });
        presentationRepository.deleteById(presentationId);
        log.info("Презентация успешно удалена");

        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }

    @Override
    public List<PresentationResponse> getAllPresentationByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () ->{log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
                    return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));});
        log.info("Получить всю презентацию по идентификатору урока: "+lessonId);
        return presentationRepository.getAllPresentationsByLessonId(lesson.getId());
    }
}