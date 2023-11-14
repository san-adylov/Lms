package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.presentationDto.PresentationRequest;
import com.app.lms.dto.presentationDto.PresentationResponse;
import com.app.lms.service.PresentationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentations")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Presentation API", description = "API for presentation CRUD management")
public class PresentationApi {

    private final PresentationService presentationService;

    @PostMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Save presentation", description = "Save presentation by lesson id")
    public SimpleResponse savePresentation(@PathVariable Long lessonId,
                                           @RequestBody PresentationRequest presentationRequest) {
        return presentationService.savePresentation(lessonId, presentationRequest);
    }

    @GetMapping("/{presentationId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get by id presentation", description = "Get presentation by id")
    public PresentationResponse getById (@PathVariable Long presentationId){
        return presentationService.getByIdPresentation(presentationId);
    }

    @PutMapping("/{presentationId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Update presentation", description = "Update presentation by id")
    public SimpleResponse updatePresentation(@PathVariable Long presentationId,
                                             @RequestBody PresentationRequest presentationRequest) {
        return presentationService.updatePresentation(presentationId, presentationRequest);
    }

    @DeleteMapping("/{presentationId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Delete presentation", description = "Delete presentation by id")
    public SimpleResponse deletePresentation(@PathVariable Long presentationId) {
        return presentationService.deletePresentation(presentationId);
    }

    @GetMapping("/getAll/{lessonId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get all presentation", description = "Get all presentation by lesson id")
    public List<PresentationResponse> getAllPresentationByLessonId(@PathVariable Long lessonId){
        return presentationService.getAllPresentationByLessonId(lessonId);
    }
}
