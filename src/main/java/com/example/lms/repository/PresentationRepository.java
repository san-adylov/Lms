package com.example.lms.repository;

import com.example.lms.entities.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<Presentation,Long> {
}
