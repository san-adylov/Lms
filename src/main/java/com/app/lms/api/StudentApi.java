package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.studentDto.PaginationStudentResponse;
import com.app.lms.dto.studentDto.StudentRequest;
import com.app.lms.dto.studentDto.StudentRequest1;
import com.app.lms.service.StudentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Student API", description = "API for student CRUD management")
public class StudentApi {

    private final StudentService studentService;

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Get all students by course id", description = "Get all students by course id")
    public PaginationStudentResponse getAllStudentsByCourseId(@PathVariable Long courseId,
                                                              @RequestParam int currentPage,
                                                              @RequestParam int pageSize) {
        return studentService.getAllStudentByCourseId(courseId, pageSize, currentPage);
    }

    @GetMapping("/getAllStudent")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Get all students", description = "Get all students")
    public PaginationStudentResponse getAllStudents(@RequestParam int currentPage,
                                                    @RequestParam int pageSize) {
        return studentService.getAllStudents(pageSize, currentPage);
    }

    @PostMapping("/{groupId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Save student", description = "Save student by group id")
    public SimpleResponse saveStudent(@PathVariable Long groupId,
                                      @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.saveStudent(groupId, studentRequest);
    }

    @GetMapping("/getById/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get by id student", description = "Get student courses by student id")
    public List<CourseResponse> getById(@PathVariable Long studentId) {
        return studentService.getById(studentId);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update student", description = "Update student by id")
    public SimpleResponse updateStudent(@PathVariable Long studentId,
                                        @RequestBody @Valid StudentRequest1 studentRequest) {
        return studentService.updateStudent(studentId, studentRequest);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Delete student", description = "Delete student by id")
    public SimpleResponse deleteStudent(@PathVariable Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @DeleteMapping("/{groupId}/{studentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete student from group", description = "Delete student from group")
    public SimpleResponse deleteStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        return studentService.deleteStudentFromGroup(groupId, studentId);
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/import")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Import excel file", description = "Import students from excel file for admin")
    public SimpleResponse importExcel(@RequestParam Long groupId,
                                      @RequestParam(name = "file", required = false) MultipartFile multipartFile) throws IOException {
        return studentService.importExcel(groupId, multipartFile);
    }
}