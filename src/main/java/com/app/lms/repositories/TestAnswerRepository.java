package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.testDto.TestResultInstructorResponse;
import com.app.lms.entities.TestAnswer;

import java.util.List;
import java.util.Optional;

public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {

    @Query("select t from TestAnswer t join t.test tt join t.student s where tt.id=:id and s.id=:id1")
    Optional<TestAnswer> findTestAnswerByTestId(@Param("id") Long testId, @Param("id1") Long studentId);

    @Query("select new com.app.lms.dto.testDto.TestResultInstructorResponse(concat(u.firstName,' ', u.lastName),ta.correct,ta.inCorrect ) from TestAnswer ta join ta.student s join s.user u join ta.test t where t.id=:id")
    List<TestResultInstructorResponse> getTestAnswerByTestId(@Param("id") Long testId);
}