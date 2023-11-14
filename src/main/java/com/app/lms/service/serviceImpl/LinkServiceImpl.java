package com.app.lms.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.linkDto.LinkRequest;
import com.app.lms.dto.linkDto.LinkResponse;
import com.app.lms.entities.Lesson;
import com.app.lms.entities.Link;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.LessonRepository;
import com.app.lms.repositories.LinkRepository;
import com.app.lms.service.LinkService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final LessonRepository lessonRepository;

    @Override
    public SimpleResponse saveLink(Long lessonId, LinkRequest linkRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
            return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
        });

        if(!lesson.getLinks().isEmpty()){
            throw new  BadRequestException("Вы можете добавить только одно значение");
        }
        Link link = new Link();
        link.setText(linkRequest.text());
        link.setLink(linkRequest.link());
        lesson.getLinks().add(link);
        link.setLesson(lesson);
        linkRepository.save(link);
        log.info("Ссылка успешно сохранена!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public List<LinkResponse> getAllLinks(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
            return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
        });

        List<Link> links = linkRepository.findAllByLessonId(lesson.getId());
        return links.stream().map(link -> new LinkResponse(link.getId(), link.getText(), link.getLink()))
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponse updateLink(Long linkId, LinkRequest linkRequest) {
        Link link = linkRepository.findById(linkId).orElseThrow(() -> {
            log.error(String.format("Ссылка с идентификатором: %s не найдена", linkId));
            return new NotFoundException(String.format("Ссылка с идентификатором: %s не найдена", linkId));
        });

        if (!link.getText().equals(linkRequest.text())) {
            link.setText(linkRequest.text());
        }
        if (!link.getLink().equals(linkRequest.link())) {
            link.setLink(linkRequest.link());
        }
        linkRepository.save(link);
        log.info("Ссылка успешно обновлена!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse deleteLinkById(Long linkId) {
        Link link = linkRepository.findById(linkId).orElseThrow(() -> {
            log.error(String.format("Ссылка с идентификатором: %s не найдена", linkId));
            return new NotFoundException(String.format("Ссылка с идентификатором: %s не найдена", linkId));
        });
        linkRepository.delete(link);
        log.info(String.format("Удалить ссылку по идентификатору: %s ", linkId));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }
}
