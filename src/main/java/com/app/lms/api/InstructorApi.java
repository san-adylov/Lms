package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.instructorDto.InstructorRequest;
import com.app.lms.dto.instructorDto.InstructorResponse;
import com.app.lms.dto.instructorDto.InstructorUpdateRequest;
import com.app.lms.service.InstructorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructors")
@CrossOrigin(value = "*",maxAge = 3600)
@Tag(name = "Instructor API", description = "API for instructor CRUD management")
public class InstructorApi {

  private final InstructorService instructorService;

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  @Operation(summary = "Get all instructors", description = "Get all instructors")
  public List<InstructorResponse> getAllInstructor() {
    return instructorService.getAllInstructors();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  @Operation(summary = "Save instructor", description = "Save instructor")
  public SimpleResponse saveInstructor(@RequestBody @Valid InstructorRequest instructorRequest) {
    return instructorService.saveInstructor(instructorRequest);
  }

  @PutMapping("{instructorId}")
  @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
  @Operation(summary = "Update instructor", description = "Update instructor")
  public SimpleResponse updateInstructor(@PathVariable Long instructorId,
      @RequestBody @Valid InstructorUpdateRequest instructorRequest) {
    return instructorService.updateInstructor(instructorId, instructorRequest);
  }

  @DeleteMapping("{instructorId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  @Operation(summary = "Delete instructor", description = "Delete instructor")
  public SimpleResponse deleteInstructor(@PathVariable Long instructorId) {
    return instructorService.deleteInstructor(instructorId);
  }

  @GetMapping("/getById/{instructorId}")
  @PreAuthorize("hasAuthority('INSTRUCTOR')")
  @Operation(summary = "Get instructor by id",description = "We can see all instructor's courses")
  public List<CourseResponse> getInstructorById(@PathVariable Long instructorId){
    return instructorService.getInstructorById(instructorId);
  }
}
