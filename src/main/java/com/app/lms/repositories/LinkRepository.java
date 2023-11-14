package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.lms.entities.Link;
import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findAllByLessonId(Long id);
}
