package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.testDto.*;
import com.app.lms.service.TestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
@CrossOrigin(origins = "*",maxAge = 3600)
@Tag(name = "Test API", description = "API for test management")
public class TestApi {

    private final TestService testService;

    @GetMapping("/getAllTests/{lessonId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get all tests", description = "Get all tests by lesson id")
    public List<GetAllTestsResponse> getAllTestsByLessonId(@PathVariable Long lessonId){
        return testService.getAllTestsByLessonId(lessonId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Create test", description = "Create test by lesson id")
    public SimpleResponse createTest(@RequestParam Long lessonId, @RequestBody TestRequest testRequest){
        return testService.createTest(lessonId, testRequest);
    }

    @GetMapping("{testId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Pass test", description = "Pass test by Student")
    public TestResponse passTest(@PathVariable Long testId){
       return testService.passTest(testId);
    }

    @PostMapping("/saveResultTest/{testId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Send result test", description = "Send result test by student")
    public TestAnswerResponse testResultForStudent(@PathVariable Long testId, @RequestBody ResultTestRequest resultTestRequest){
        return testService.ResultTestOfStudent(testId, resultTestRequest);
    }

    @GetMapping("testResult/{testId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Get result test", description = "Get list of students' results by instructor")
    public TestResultResponse resultResponseForInstructor(@PathVariable Long testId){
        return testService.testResultForInstructor(testId);
    }

    @PostMapping("isAccepted/{testId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Is accepted", description = "Is accepted result of test")
    public SimpleResponse isAccepted(@PathVariable Long testId, @RequestParam boolean isAccepted, @RequestParam String text){
        return testService.isAccepted(testId, isAccepted, text);
    }

    @GetMapping("getResultTest/{testId}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @Operation(summary = "Get result test for student" ,description = "Get result test for student ")
    public TestResultStudentResponse getTestResult(@PathVariable Long testId){
       return testService.resultTest(testId);
    }

    @PutMapping("/{testId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Edit test" ,description = "Edit test and questions and options by test id")
    public SimpleResponse updateTest(@PathVariable Long testId, @RequestBody TestRequest testRequest){
        return testService.updateTest(testId, testRequest);
    }

    @DeleteMapping("/{testId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Operation(summary = "Delete test", description = "delete test by test id")
    public SimpleResponse deleteTest(@PathVariable Long testId){
        return testService.deleteTest(testId);
    }
}
