package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.testDto.OptionResponse;
import com.app.lms.dto.testDto.TestResultQuestionResponse;
import com.app.lms.entities.QuestionAnswer;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    @Query("SELECT NEW com.app.lms.dto.testDto.TestResultQuestionResponse(q.id,q.question.questionName,q.point,q.question.id,q.question.questionType) FROM QuestionAnswer q  WHERE q.testAnswer.id=:id")
    List<TestResultQuestionResponse> getAllQuestionAnswersOfTestAnswer(@Param("id") Long testAnswerId);

    @Query("select new com.app.lms.dto.testDto.OptionResponse(o.id,o.option,o.isTrue) from QuestionAnswer q join q.option o where q.id=:id")
    List<OptionResponse> getAllOptionsOfQuestionAnswer(@Param("id") Long questionAnswerId);
}