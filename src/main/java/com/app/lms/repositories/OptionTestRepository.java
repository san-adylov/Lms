package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.testDto.OptionResponse;
import com.app.lms.entities.OptionTest;

import java.util.List;

public interface OptionTestRepository extends JpaRepository<OptionTest, Long> {

    @Query("SELECT NEW com.app.lms.dto.testDto.OptionResponse(o.id,o.option,o.isTrue) FROM OptionTest o WHERE o.question.id=:id")
    List<OptionResponse> getOptionTestByQuestionId(@Param("id") Long questionId);
}