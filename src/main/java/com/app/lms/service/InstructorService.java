package com.app.lms.service;

import java.util.List;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.instructorDto.InstructorRequest;
import com.app.lms.dto.instructorDto.InstructorResponse;
import com.app.lms.dto.instructorDto.InstructorUpdateRequest;

public interface InstructorService {

  List<InstructorResponse> getAllInstructors();

  SimpleResponse saveInstructor(InstructorRequest instructorRequest);

  SimpleResponse updateInstructor(Long id, InstructorUpdateRequest instructorUpdateRequest);

  SimpleResponse deleteInstructor(Long id);

  List<CourseResponse> getInstructorById(Long id);

}
