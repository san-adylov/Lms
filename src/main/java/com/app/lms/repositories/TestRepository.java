package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.testDto.GetAllTestsResponse;
import com.app.lms.entities.Test;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("select new com.app.lms.dto.testDto.GetAllTestsResponse(t.id,t.name) from Test  t join t.lesson l where l.id=:id")
    List<GetAllTestsResponse> getAllTestByLessonId(@Param("id") Long lessonID);
}