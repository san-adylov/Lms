package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.app.lms.entities.TaskAnswer;

public interface TaskAnswerRepository extends JpaRepository<TaskAnswer,Long> {

    @Query("select t from TaskAnswer t join t.task tt join t.student s where tt.id =:id and s.id =:id1")
    TaskAnswer getTaskAnswerByTaskId(@Param("id") Long taskId, @Param("id1") Long studentId);
}
