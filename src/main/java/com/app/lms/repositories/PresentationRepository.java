package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.presentationDto.PresentationResponse;
import com.app.lms.entities.Presentation;

import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    @Query("SELECT new com.app.lms.dto.presentationDto.PresentationResponse(p.id,p.name, p.description, p.linkFilePpt) FROM Presentation p JOIN p.lesson l WHERE l.id = :lessonId")
    List<PresentationResponse> getAllPresentationsByLessonId(@Param("lessonId") Long lessonId);
}
