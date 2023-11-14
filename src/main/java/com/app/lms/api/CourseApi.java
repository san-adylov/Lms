package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.AssigningInstructorsRequest;
import com.app.lms.dto.courseDto.CourseRequest;
import com.app.lms.dto.courseDto.CourseResponseGroup;
import com.app.lms.dto.groupDto.GroupResponseCourse;
import com.app.lms.dto.instructorDto.InstructorResponseGroup;
import com.app.lms.dto.studentDto.StudentResponse;
import com.app.lms.service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@CrossOrigin(value = "*", maxAge = 3600)
@Tag(name = "Course API", description = "API for course CRUD management")
public class CourseApi {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Save course", description = "Save course")
    public SimpleResponse save(@RequestBody @Valid CourseRequest courseRequest) {
        return courseService.saveCourse(courseRequest);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all courses", description = "Get all courses")
    public List<CourseResponseGroup> getAll() {
        return courseService.getAll();
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update course", description = "Update course by id")
    public SimpleResponse update(@PathVariable Long courseId, @RequestBody @Valid CourseRequest courseRequest) {
        return courseService.updateCourse(courseId, courseRequest);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete course", description = "Delete course by id")
    public SimpleResponse delete(@PathVariable Long courseId) {
        return courseService.deleteCourseById(courseId);
    }

    @GetMapping("/getInstructors/{courseId}")
    @Operation(summary = "Get course's instructors", description = "Get course's instructors by course id")
    public List<InstructorResponseGroup> getInsByCourseId(@PathVariable Long courseId) {
        return courseService.getAllInstructorsByCourseId(courseId);
    }

    @GetMapping("/getStudents/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get course's students", description = "Get course's students by course id")
    public List<StudentResponse> getStudentsByCourseId(@PathVariable Long courseId) {
        return courseService.getAllStudentsByCourseId(courseId);
    }

    @PostMapping("/{groupId}/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Add group to course", description = "Add group to course by id")
    public SimpleResponse addGroupToCourse(@PathVariable Long groupId, @PathVariable Long courseId) {
        return courseService.addGroupToCourse(groupId, courseId);
    }

    @DeleteMapping("/{groupId}/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Delete group from course", description = "Delete group from course by id")
    public SimpleResponse deleteGroupToCourse(@PathVariable Long groupId, @PathVariable Long courseId) {
        return courseService.deleteGroupToCourse(groupId, courseId);
    }

    @PostMapping("/assign/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Assigning instructor", description = "Assign instructor to course")
    public SimpleResponse assignInstructorToCourse(@PathVariable Long courseId, @RequestBody AssigningInstructorsRequest request) {
        return courseService.assignInstructorToCourse(courseId, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/instructor/{courseId}/{instructorId}")
    @Operation(summary = "Delete instructor", description = "Delete instructor from course")
    public SimpleResponse deleteInstructorFromCourse(@PathVariable Long courseId, @PathVariable Long instructorId) {
        return courseService.deleteInstructorFromCourse(courseId, instructorId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/instructors/{courseId}")
    @Operation(summary = "Delete all instructors", description = "Delete all instructors from course")
    public SimpleResponse deleteInstructors(@PathVariable Long courseId) {
        return courseService.deleteInstructorsFromCourse(courseId);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get group", description = "Get group by course id")
    public GroupResponseCourse getGroup(@PathVariable Long courseId) {
        return courseService.getGroupByCourseId(courseId);
    }
}