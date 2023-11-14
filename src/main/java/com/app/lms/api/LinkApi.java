package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.linkDto.LinkRequest;
import com.app.lms.dto.linkDto.LinkResponse;
import com.app.lms.service.LinkService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/links")
@CrossOrigin(value = "*", maxAge = 3600)
@Tag(name = "Link API", description = "API for link CRUD management")
public class LinkApi {

    private final LinkService linkService;

    @PostMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Save link", description = "Save link by lesson id")
    public SimpleResponse save(@PathVariable Long lessonId, @RequestBody LinkRequest linkRequest) {
        return linkService.saveLink(lessonId, linkRequest);
    }

    @PreAuthorize("hasAnyAuthority('INSTRUCTOR', 'STUDENT')")
    @GetMapping("/{lessonId}")
    @Operation(summary = "Get All links", description = "Get all links by lesson id")
    public List<LinkResponse> getAll(@PathVariable Long lessonId) {
        return linkService.getAllLinks(lessonId);
    }

    @PutMapping("/{linkId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Update link", description = "Update link by id")
    public SimpleResponse update(@PathVariable Long linkId, @RequestBody LinkRequest linkRequest) {
        return linkService.updateLink(linkId, linkRequest);
    }

    @DeleteMapping("/{linkId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Delete link", description = "Delete link by id")
    public SimpleResponse delete(@PathVariable Long linkId) {
        return linkService.deleteLinkById(linkId);
    }

}
