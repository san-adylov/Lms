package com.example.lms.repository;

import com.example.lms.entities.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAnswerRepository extends JpaRepository<TestAnswer,Long> {
}
