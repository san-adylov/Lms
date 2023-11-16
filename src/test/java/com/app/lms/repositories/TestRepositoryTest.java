package com.app.lms.repositories;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.testDto.*;
import com.app.lms.entities.*;
import com.app.lms.enums.QuestionType;
import com.app.lms.enums.Role;
import com.app.lms.enums.StudyFormat;
import com.app.lms.service.serviceImpl.TestServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DataJpaTest
@RequiredArgsConstructor
class TestRepositoryTest {

   @Mock
   private LessonRepository lessonRepository;

   @Mock
   private UserRepository userRepository;

   @Mock
   private TestRepository testRepository;

   @Mock
   private QuestionRepository questionRepository;

   @Mock
   private OptionTestRepository optionTestRepository;

   @Mock
   private StudentRepository studentRepository;
   @Mock
   private TestAnswerRepository testAnswerRepository;
   @Mock
   private QuestionAnswerRepository questionAnswerRepository;
   @InjectMocks
   private TestServiceImpl testService;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(testRepository);
   }

   @Test
   void createTest(){
      Long lessonId = 3L;
      List<QuestionRequest> questionRequests = new ArrayList<>();
      TestRequest testRequest = new TestRequest("Simple test",questionRequests);
      Lesson lesson = new Lesson();
      lesson.setId(lessonId);
      lesson.setLessonName("lesson");
      when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
      com.app.lms.entities.Test test = new com.app.lms.entities.Test();
      when(testRepository.save(any(com.app.lms.entities.Test.class))).thenReturn(test);
      SimpleResponse response = testService.createTest(lessonId, testRequest);
      assertEquals(HttpStatus.OK, response.getStatus());
      assertEquals("Success", response.getMessage());
      verify(lessonRepository, times(1)).findById(lessonId);
      verify(testRepository, times(1)).save(any(com.app.lms.entities.Test.class));
      verify(questionRepository, times(questionRequests.size())).save(any(Question.class));
   }

   @Test
   void passTest(){
      Long testId= 1L;
      List<QuestionResponse>questionResponses=new ArrayList<>();
      com.app.lms.entities.Test test = new com.app.lms.entities.Test();
      test.setId(testId);
      test.setName("Core1");
      test.setEnable(false);
      when(testRepository.findById(testId)).thenReturn(Optional.of(test));
      when(questionRepository.getQuestionsByTestId(testId)).thenReturn(questionResponses);
      List<OptionResponse> optionResponses = new ArrayList<>();
      for (QuestionResponse q:questionResponses) {
         when(optionTestRepository.getOptionTestByQuestionId(q.getId())).thenReturn(optionResponses);
      }
      TestResponse response = testService.passTest(testId);
      assertNotNull(response);
      assertEquals("Core1",test.getName());
      assertEquals(questionResponses,response.getQuestionResponses());
      verify(testRepository, times(1)).findById(testId);
      verify(questionRepository, times(1)).getQuestionsByTestId(testId);
      for (QuestionResponse q: questionResponses) {
         verify(optionTestRepository, times(1)).getOptionTestByQuestionId(q.getId());
      }
   }

   @Test
   void resultTestOfStudentTest(){
      Long testId = 1L;
      Long userId = 2L;
      Long questionId = 3L;
      Long optionId = 4L;
      List<ResultTestQuestionRequest> RTQR = new ArrayList<>();
      ResultTestRequest resultTestRequest = new ResultTestRequest(RTQR);
      com.app.lms.entities.Test test = new com.app.lms.entities.Test();
      test.setId(testId);
      test.setName("Core1");
      test.setEnable(false);
      when(testRepository.findById(testId)).thenReturn(Optional.of(test));
      String email = "user@gmail.com";
      User user = new User();
      user.setId(userId);
      user.setRole(Role.STUDENT);
      user.setEmail(email);
      user.setPassword("user123");
      user.setFirstName("Bek");
      user.setLastName("Tilekov");
      user.setPhoneNumber("+996221010101");
      when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(user.getEmail());
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      SecurityContextHolder.setContext(securityContext);
      Student student = new Student();
      student.setId(5L);
      student.setUser(user);
      student.setBlocked(false);
      when(studentRepository.getStudentsByUserId(userId)).thenReturn(student);
      TestAnswer testAnswer = new TestAnswer();
      testAnswer.setTestName(test.getName());
      testAnswer.setStudent(student);
      testAnswer.setCorrect(4);
      testAnswer.setInCorrect(2);
      testAnswer.setAccepted(false);
      testAnswer.setPoint(40);
      testAnswer.setId(6L);
      when(testAnswerRepository.save(any(TestAnswer.class))).thenReturn(testAnswer);
      Question question = new Question();
      question.setId(questionId);
      question.setTest(test);
      question.setQuestionType(QuestionType.SINGLE);
      question.setQuestionName("What is OOP?");
      when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
      OptionTest optionTest = new OptionTest();
      optionTest.setOption("Java");
      optionTest.setTrue(true);
      optionTest.setId(optionId);
      optionTest.setQuestion(question);
      when(optionTestRepository.findAllById(Collections.singleton(optionId))).thenReturn(List.of(optionTest));
      TestAnswerResponse response = testService.ResultTestOfStudent(testId,resultTestRequest);
      assertNotNull(response);
      verify(testRepository, times(1)).findById(testId);
      verify(studentRepository, times(1)).getStudentsByUserId(userId);
      verify(testAnswerRepository,times(2)).save(any(TestAnswer.class));
      verify(questionRepository, times(resultTestRequest.questionsId().size())).findById(anyLong());
      verify(questionAnswerRepository, times(resultTestRequest.questionsId().size())).save(any(QuestionAnswer.class));
   }

   @Test
   void testResultForInstructorTest(){
      Long testId = 1L;
      Long studentId = 2L;
      List<TestAnswer> testAnswers = new ArrayList<>();
      Student student = new Student();
      student.setId(studentId);
      student.setStudyFormat(StudyFormat.OFFLINE);
      student.setUser(new User());
      when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
      com.app.lms.entities.Test test = new com.app.lms.entities.Test();
      test.setId(testId);
      test.setName("Core1");
      test.setEnable(false);
      test.setTestAnswers(testAnswers);
      test.setQuestions(new ArrayList<>());
      TestAnswer testAnswer = new TestAnswer();
      testAnswer.setTest(test);
      testAnswer.setStudent(student);
      testAnswers.add(testAnswer);
      when(testRepository.findById(testId)).thenReturn(Optional.of(test));
      TestResultResponse testResultResponse = testService.testResultForInstructor(testId);
      assertEquals(test.isEnable(),testResultResponse.isAccepted());
      assertEquals(testAnswers.size(),testResultResponse.getTestResultsListOFStudents().size());
      verify(testRepository,times(1)).findById(testId);
   }

   @Test
   void isAcceptedTest(){
      boolean isAvailable = true;
      Long testId = 1L;
      com.app.lms.entities.Test test = new com.app.lms.entities.Test();
      test.setId(testId);
      test.setName("Core1");
      test.setEnable(false);
      String text = "accepted";
      when(testRepository.findById(testId)).thenReturn(Optional.of(test));
      SimpleResponse response = testService.isAccepted(testId,isAvailable, text);
      assertEquals(HttpStatus.OK,response.getStatus());
      assertEquals("Success",response.getMessage());
      assertEquals(isAvailable, test.isEnable());
      verify(testRepository,times(1)).findById(testId);
   }
}