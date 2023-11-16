package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.videoLessonDto.VideoLessonRequest;
import com.app.lms.dto.videoLessonDto.VideoLessonResponse;
import com.app.lms.service.VideoLessonService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "VideoLesson API", description = "API for videoLesson CRUD management")
public class VideoLessonApi {

    private final VideoLessonService videoLessonService;

    @PostMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Save videoLesson", description = "To save a new videoLesson by Instructor")
    public SimpleResponse saveVideo(@PathVariable Long lessonId,@RequestBody VideoLessonRequest videoLessonRequest) {
        return videoLessonService.saveVideoLesson(lessonId,videoLessonRequest);
    }

    @PutMapping("/{videoLessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Update videoLesson", description = "Instructor update videoLesson by id")
    public SimpleResponse updateVideo(@PathVariable Long videoLessonId, @RequestBody VideoLessonRequest videoLessonRequest) {
        return videoLessonService.updateVideoLesson(videoLessonId, videoLessonRequest);
    }

    @DeleteMapping("/{videoLessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Delete videoLesson", description = "Instructor delete videoLesson by id")
    public SimpleResponse deleteVideo(@PathVariable Long videoLessonId) {
        return videoLessonService.deleteVideoLessonById(videoLessonId);
    }

    @GetMapping("/{videoLessonId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get videoLesson by id", description = "Instructor get videoLesson by id")
    public VideoLessonResponse getVideoById(@PathVariable Long videoLessonId) {
        return videoLessonService.getVideoLessonById(videoLessonId);
    }

    @GetMapping("/{lessonId}/lesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get all videoLessons", description = "Instructor get all videoLessons")
    public List<VideoLessonResponse> getAllVideoLessons(@PathVariable Long lessonId) {
        return videoLessonService.getAllVideoLesson(lessonId);
    }
}

