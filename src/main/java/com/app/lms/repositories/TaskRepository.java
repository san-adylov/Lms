package com.app.lms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.dto.taskDto.TaskGetAllResponse;
import com.app.lms.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select new com.app.lms.dto.taskDto.TaskGetAllResponse(t.id,t.taskName) from Task t where t.lesson.id = :lessonId")
    List<TaskGetAllResponse> getAllTasks(@Param("lessonId") Long lessonId);
}
