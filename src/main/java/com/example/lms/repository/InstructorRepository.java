package com.example.lms.repository;

import com.example.lms.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> getInstructorByUserId(Long userId);
}
