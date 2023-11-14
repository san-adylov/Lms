package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.presentationDto.PresentationRequest;
import com.app.lms.dto.presentationDto.PresentationResponse;

import java.util.List;

public interface PresentationService {

    SimpleResponse savePresentation(Long lessonId, PresentationRequest presentationRequest);

    PresentationResponse getByIdPresentation(Long presentationId);

    SimpleResponse updatePresentation(Long presentationId, PresentationRequest presentationRequest);

    SimpleResponse deletePresentation(Long presentationId);

    List<PresentationResponse> getAllPresentationByLessonId(Long lessonId);
}
