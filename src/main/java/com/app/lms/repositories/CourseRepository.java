package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.entities.Course;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseName(String courseName);

    @Query(" SELECT NEW com.app.lms.dto.courseDto.CourseResponse (c.id, c.courseName, c.image, c.description,c.dateOfGraduation,g.id,g.groupName) FROM Course c FULL JOIN c.instructors i full join c.group g WHERE i.id = :id ")
    List<CourseResponse> courses(@Param("id") Long instructorId);

    @Query("""
            SELECT NEW
             com.app.lms.dto.courseDto.CourseResponse
             (c.id, c.courseName,c.image,c.description,c.dateOfGraduation,g.id,g.groupName)
            FROM Course c
            JOIN c.group g
            JOIN g.students s
            WHERE s.id = ?1 order by c.id desc 
            """)
    List<CourseResponse> getAllStudentCourses( Long studentId);

    @Query("SELECT c FROM Course c LEFT JOIN c.group g")
    List<Course> getAllCoursesWithGroups();
}
