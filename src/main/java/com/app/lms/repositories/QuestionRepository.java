package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.testDto.QuestionResponse;
import com.app.lms.entities.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT NEW com.app.lms.dto.testDto.QuestionResponse(q.id,q.questionName,q.questionType) FROM Question q WHERE q.test.id=:id")
    List<QuestionResponse> getQuestionsByTestId(@Param("id") Long testId);

}