package com.app.lms.service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.testDto.*;
import com.app.lms.entities.*;
import com.app.lms.enums.QuestionType;
import com.app.lms.exceptions.BadCredentialException;
import com.app.lms.exceptions.BadRequestException;
import com.app.lms.exceptions.NotFoundException;
import com.app.lms.repositories.*;
import com.app.lms.service.TestService;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final StudentRepository studentRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final TestAnswerRepository testAnswerRepository;
    private final OptionTestRepository optionTestRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String senderEmail;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.getUserByEmail(name)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ником: %s не найден!", name)));
    }

    @Override
    public List<GetAllTestsResponse> getAllTestsByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> {
                    log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
                    return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
                });
        log.info("Получить все тесты по идентификатору урока:" + lessonId);
        return testRepository.getAllTestByLessonId(lesson.getId());
    }

    @Override
    public SimpleResponse createTest(Long lessonId, TestRequest testRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            log.error(String.format("Урок с идентификатором: %s не найден", lessonId));
            return new NotFoundException(String.format("Урок с идентификатором: %s не найден", lessonId));
        });
        if (testRequest.testName().isEmpty() && testRequest.testName().isBlank()) {
            log.error("Название теста не должно быть пустым");
            throw new BadRequestException("Название теста не должно быть пустым");
        }
        Test test = new Test();
        test.setTestAnswers(new ArrayList<>());
        test.setQuestions(new ArrayList<>());
        test.setName(testRequest.testName());
        testRepository.save(test);
        test.setLesson(lesson);
        for (QuestionRequest questionRequest : testRequest.questionRequests()) {
            List<OptionTest> optionTests = new ArrayList<>();
            Question question = new Question();
            question.setQuestionName(questionRequest.question());
            question.setQuestionType(questionRequest.questionType());
            question.setOptionTests(optionTests);
            questionRepository.save(question);
            question.setTest(test);
            test.getQuestions().add(question);
            for (OptionRequest option : questionRequest.optionRequests()) {
                if (option.option().isBlank() && option.option().isEmpty()) {
                    log.error("Вариант не должен быть пустым");
                    throw new BadRequestException("Вариант не должен быть пустым");
                }
                OptionTest optionTest = new OptionTest();
                optionTest.setOption(option.option());
                optionTest.setTrue(option.isStatus());
                optionTestRepository.save(optionTest);
                optionTest.setQuestion(question);
                question.getOptionTests().add(optionTest);
            }
        }
        log.info("Тест создан");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public TestResponse passTest(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тест с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тест с идентификатором: %s не найден", testId));
        });
        User user = getAuthentication();
        Student student = studentRepository.getStudentsByUserId(user.getId());
        List<TestAnswer> testAnswers = testAnswerRepository.findAll();
        for (TestAnswer a : testAnswers) {
            if (a.getTest().equals(test) && a.getStudent().equals(student)) {
                log.info("Студент уже сдал тест");
                return TestResponse.builder()
                        .testName(test.getName())
                        .isPassed(true)
                        .questionResponses(new ArrayList<>())
                        .build();
            }
        }
        List<QuestionResponse> questions = questionRepository.getQuestionsByTestId(testId);
        for (QuestionResponse q : questions) {
            List<OptionResponse> options = optionTestRepository.getOptionTestByQuestionId(q.getId());
            q.setOptionResponses(options);
        }
        log.info("Пройти тест как студент");
        return TestResponse.builder()
                .testName(test.getName())
                .isPassed(false)
                .questionResponses(questions)
                .build();
    }

    @Override
    public TestAnswerResponse ResultTestOfStudent(Long testId, ResultTestRequest resultTestRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тест с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тест с идентификатором: %s не найден", testId));
        });
        User user = getAuthentication();
        Student student = studentRepository.getStudentsByUserId(user.getId());
        for (TestAnswer t : test.getTestAnswers()) {
            if (t.getStudent().equals(student)) {
                throw new BadRequestException("Вы уже сдали этот тест");
            }
        }
        if (!test.isEnable()) {
            TestAnswer testAnswer = new TestAnswer();
            testAnswer.setTestName(test.getName());
            testAnswer.setAccepted(false);
            testAnswerRepository.save(testAnswer);
            testAnswer.setStudent(student);
            testAnswer.setTest(test);
            double totalPoint = 0;
            int correctAnswers = 0;
            int inCorrectAnswers = 0;
            List<QuestionAnswer> questionAnswers = new ArrayList<>();
            for (ResultTestQuestionRequest q : resultTestRequest.questionsId()) {
                double point = 0;
                Question question = questionRepository.findById(q.questionId()).orElseThrow(() -> new NotFoundException("Вопрос не найден "));
                List<OptionTest> options = optionTestRepository.findAllById(q.optionId());
                QuestionAnswer questionAnswer = new QuestionAnswer();
                questionAnswer.setQuestion(question);
                questionAnswer.setOption(options);
                questionAnswer.setTestAnswer(testAnswer);
                questionAnswerRepository.save(questionAnswer);
                questionAnswers.add(questionAnswer);
                if (q.optionId().isEmpty()) {
                    throw new BadRequestException("Выберите хотя бы 1 вариант");
                }
                if (questionAnswer.getQuestion().getQuestionType().equals(QuestionType.SINGLE)) {
                    for (OptionTest o : questionAnswer.getOption()) {
                        if (o.isTrue()) {
                            correctAnswers++;
                            point += 10;
                        } else {
                            inCorrectAnswers++;
                        }
                    }
                } else if (questionAnswer.getQuestion().getQuestionType().equals(QuestionType.MULTIPLE)) {
                    double counter = 0;
                    double counter1 = 0;
                    if (question.getOptionTests().size() != q.optionId().size()) {
                        if (questionAnswer.getOption().size() >= 2) {
                            for (OptionTest o1 : questionAnswer.getOption()) {
                                if (o1.isTrue()) {
                                    counter++;
                                } else {
                                    counter1++;
                                }
                            }
                            if (questionAnswer.getOption().size() == counter) {
                                point += 10;
                                correctAnswers++;
                            } else if (counter < questionAnswer.getOption().size() && counter >= 2) {
                                correctAnswers++;
                                point += 10 / counter;
                            } else if (counter == 1) {
                                double counter2 = 0;
                                for (OptionTest o : questionAnswer.getQuestion().getOptionTests()) {
                                    if (o.isTrue()) {
                                        counter2++;
                                    }
                                }
                                point += 10 / counter2;
                                inCorrectAnswers++;
                            } else if (counter1 == questionAnswer.getOption().size()) {
                                inCorrectAnswers++;
                            } else {
                                inCorrectAnswers++;
                            }
                        } else {
                            if (questionAnswer.getOption().get(0).isTrue()) {
                                double counter2 = 0;
                                for (OptionTest o : questionAnswer.getQuestion().getOptionTests()) {
                                    if (o.isTrue()) {
                                        counter2++;
                                    }
                                }
                                point += 10 / counter2;
                                correctAnswers++;
                            } else {
                                inCorrectAnswers++;
                            }
                        }
                    }else {
                        inCorrectAnswers++;
                        point = 0;
                    }
                }
                questionAnswer.setPoint(point);
                totalPoint += questionAnswer.getPoint();
            }
            testAnswer.setPoint(totalPoint);
            testAnswer.setCorrect(correctAnswers);
            testAnswer.setInCorrect(inCorrectAnswers);
            testAnswer.setQuestionAnswers(questionAnswers);
            testAnswerRepository.save(testAnswer);
            log.info("Результат теста для студента");
            return TestAnswerResponse.builder()
                    .testAnswerId(testAnswer.getId())
                    .build();
        } else throw new BadCredentialException("Ответы больше не принимаются!");
    }

    @Override
    public TestResultResponse testResultForInstructor(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
        });
        if (test.getTestAnswers() == null) {
            return TestResultResponse
                    .builder()
                    .isAccepted(test.isEnable())
                    .quantityOfTestAnswer(test.getTestAnswers().size())
                    .testResultsListOFStudents(new ArrayList<>())
                    .build();
        }
        if (test.getTestAnswers().isEmpty()) {
            return TestResultResponse
                    .builder()
                    .isAccepted(test.isEnable())
                    .quantityOfTestAnswer(test.getTestAnswers().size())
                    .testResultsListOFStudents(new ArrayList<>())
                    .build();
        }
        List<TestResultInstructorResponse> testAnswers = testAnswerRepository.getTestAnswerByTestId(test.getId());
        for (TestResultInstructorResponse t : testAnswers) {
            t.setPoint((t.getCorrectAnswers() / test.getQuestions().size()) * 100 / 10);
        }
        log.info("Получите результат теста для инструктора");
        return TestResultResponse
                .builder()
                .isAccepted(test.isEnable())
                .quantityOfTestAnswer(test.getTestAnswers().size())
                .testResultsListOFStudents(testAnswers)
                .build();
    }

    @Override
    public SimpleResponse isAccepted(Long testId, boolean isAvailable, String text) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
        });

        if (!isAvailable) {
            test.setEnable(false);
        }
        if (isAvailable) {
            test.setEnable(true);
            try {
                for (Student s : test.getLesson().getCourse().getGroup().getStudents()) {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                    helper.setFrom(senderEmail);
                    helper.setTo(s.getUser().getEmail());
                    helper.setSubject("Доступ на тест");
                    Context context = new Context();
                    context.setVariable("text", text);
                    String htmlContent = templateEngine.process("isAccaptableTest", context);
                    helper.setText(htmlContent, true);
                    javaMailSender.send(message);
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw new RuntimeException(exception.getMessage());
            }

        }
        log.info("Изменение статуса, включения или отключения теста");
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Success")
                .build();
    }

    @Override
    public TestResultStudentResponse resultTest(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
        });
        User user = getAuthentication();
        Student student = studentRepository.getStudentsByUserId(user.getId());
        if (student==null){
            throw new NotFoundException("Студент не найден");
        }
        TestAnswer testAnswer = testAnswerRepository.findTestAnswerByTestId(test.getId(), student.getId()).orElseThrow(() -> {
            log.error("Ответ на тест не найден");
            return new NotFoundException("Ответ на тест не найден");
        });
        List<TestResultQuestionResponse> questionResponses1 = questionAnswerRepository.getAllQuestionAnswersOfTestAnswer(testAnswer.getId());
        for (TestResultQuestionResponse q : questionResponses1) {
            List<OptionResponse> options = questionAnswerRepository.getAllOptionsOfQuestionAnswer(q.getId());
            List<OptionResponse> optionTest = optionTestRepository.getOptionTestByQuestionId(q.getQuestionId());
            for (OptionResponse o : options) {
                for (OptionResponse ot : optionTest) {
                    if (ot.getOptionId().equals(o.getOptionId())) {
                        ot.setChosen(true);
                    }
                }
            }
            q.setOptionResponses(optionTest);
        }
        return TestResultStudentResponse
                .builder()
                .totalPoint(testAnswer.getPoint())
                .questionResponses(questionResponses1)
                .build();
    }

    @Override
    public SimpleResponse updateTest(Long testId, TestRequest testRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
        });
        if (!test.getName().equals(testRequest.testName())) {
            test.setName(testRequest.testName());
        }
        for (QuestionRequest qr : testRequest.questionRequests()) {
            for (Question q : test.getQuestions()) {
                if (qr.questionId().equals(q.getId())) {
                    q.setQuestionName(qr.question());
                    q.setQuestionType(qr.questionType());
                    for (OptionRequest o : qr.optionRequests()) {
                        for (OptionTest ot : q.getOptionTests()) {
                            if (o.optionId().equals(ot.getId())) {
                                ot.setOption(o.option());
                                ot.setTrue(o.isStatus());
                            }
                        }
                    }
                }
            }
        }
        log.info(String.format("Тест с идентификатором: %s успешно обновлен", test.getId()));
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }

    @Override
    public SimpleResponse deleteTest(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
            return new NotFoundException(String.format("Тестовый ответ с идентификатором: %s не найден", testId));
        });
        test.getLesson().getTests().remove(test);
        testRepository.deleteById(testId);
        log.info(String.format("Тест с идентификатором: %s успешно удален", test.getId()));
        return SimpleResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Успешно")
                .build();
    }
}
