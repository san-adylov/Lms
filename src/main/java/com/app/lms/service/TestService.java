package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.testDto.*;

import java.util.List;

public interface TestService {
    List<GetAllTestsResponse> getAllTestsByLessonId(Long lessonId);

    SimpleResponse createTest(Long lessonId, TestRequest testRequest);

    TestResponse passTest(Long testId);

    TestAnswerResponse ResultTestOfStudent(Long testId, ResultTestRequest resultTestRequest);

    TestResultResponse testResultForInstructor(Long testId);

    SimpleResponse isAccepted(Long testId,boolean isAvailable,String text);

    TestResultStudentResponse resultTest(Long testId);

    SimpleResponse updateTest(Long testId ,TestRequest testRequest);
    SimpleResponse deleteTest(Long testId);


}
