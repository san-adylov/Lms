package com.app.lms.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.studentDto.StudentResponse;
import com.app.lms.entities.Student;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(" SELECT  NEW com.app.lms.dto.studentDto.StudentResponse(s.id,concat(u.firstName,' ',u.lastName),s.group.groupName ,u.email,u.password,u.phoneNumber,s.studyFormat) FROM Student  s  JOIN s.group g JOIN g.courses c JOIN s.user u WHERE c.id =:courseId order by s.id desc ")
    Page<StudentResponse> getAllStudentsByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT NEW com.app.lms.dto.studentDto.StudentResponse(s.id, concat(s.user.firstName,' ', s.user.lastName),s.group.groupName  , s.user.email, s.user.password, s.user.phoneNumber, s.studyFormat) FROM Student s order by s.id desc ")
    Page<StudentResponse> getAllStudents(Pageable pageable);

    @Query("SELECT s FROM Student s where s.user.id=:id")
    Student getStudentsByUserId(@Param("id") Long userId);

    @Query("select s from Student s join s.group g where g.id=:id")
    List<Student> findByGroupId(@Param("id") Long groupId);

    @Query("select new com.app.lms.dto.studentDto.StudentResponse(s.id, concat(s.user.firstName,' ', s.user.lastName),s.group.groupName  , s.user.email, s.user.password, s.user.phoneNumber, s.studyFormat) from Student s join s.group.students sg where sg.id=:id")
    List<StudentResponse> getStudentByCourseId(@Param("id") Long courseId);
}
