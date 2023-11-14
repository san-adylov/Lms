package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.AssigningInstructorsRequest;
import com.app.lms.dto.courseDto.CourseRequest;
import com.app.lms.dto.courseDto.CourseResponseGroup;
import com.app.lms.dto.groupDto.GroupResponseCourse;
import com.app.lms.dto.instructorDto.InstructorResponseGroup;
import com.app.lms.dto.studentDto.StudentResponse;

import java.util.List;

public interface CourseService {

    SimpleResponse saveCourse(CourseRequest courseRequest);

    List<CourseResponseGroup> getAll();

    SimpleResponse updateCourse(Long id, CourseRequest courseRequest);

    SimpleResponse deleteCourseById(Long id);

    List<InstructorResponseGroup> getAllInstructorsByCourseId(Long courseId);

    List<StudentResponse> getAllStudentsByCourseId(Long courseId);

    SimpleResponse addGroupToCourse(Long groupId, Long courseId);

    SimpleResponse deleteGroupToCourse(Long groupId, Long courseId);

    SimpleResponse deleteInstructorsFromCourse(Long courseId);

    SimpleResponse assignInstructorToCourse(Long courseId, AssigningInstructorsRequest assigningInstructorsRequest);

    SimpleResponse deleteInstructorFromCourse(Long courseId, Long instructorId);

    GroupResponseCourse getGroupByCourseId(Long courseId);

}
