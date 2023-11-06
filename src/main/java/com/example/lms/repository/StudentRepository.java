package com.example.lms.repository;

import com.example.lms.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student getStudentsByUserId(Long userId);
}
