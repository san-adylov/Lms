package com.app.lms.dto.presentationDto;

public record PresentationRequest(
        String name,
        String description,
        String linkPptFile
) {
}
