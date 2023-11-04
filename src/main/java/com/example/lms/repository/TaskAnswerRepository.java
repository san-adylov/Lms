package com.example.lms.repository;

import com.example.lms.entities.TaskAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAnswerRepository extends JpaRepository<TaskAnswer,Long> {
}
