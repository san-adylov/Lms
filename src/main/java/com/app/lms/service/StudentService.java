package com.app.lms.service;

import org.springframework.web.multipart.MultipartFile;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.courseDto.CourseResponse;
import com.app.lms.dto.studentDto.PaginationStudentResponse;
import com.app.lms.dto.studentDto.StudentRequest;
import com.app.lms.dto.studentDto.StudentRequest1;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    SimpleResponse saveStudent(Long groupId, StudentRequest studentRequest);

    PaginationStudentResponse getAllStudentByCourseId(Long courseId, int pageSize, int CurrentPage);

    PaginationStudentResponse getAllStudents(int pageSize,int currenPage);

    List<CourseResponse> getById(Long studentId);

    SimpleResponse updateStudent(Long studentId, StudentRequest1 studentRequest);

    SimpleResponse importExcel(Long groupId, MultipartFile multipartFile) throws IOException;

    SimpleResponse deleteStudent(Long studentId);

    SimpleResponse deleteStudentFromGroup(Long groupId,Long studentId);
}
