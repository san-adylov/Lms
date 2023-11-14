package com.app.lms.repositories.impl;

import org.springframework.stereotype.Repository;
import com.app.lms.repositories.InstructorJdbcTemplateQueryRepository;
@Repository
public class InstructorJdbcTemplateQueriesImpl implements InstructorJdbcTemplateQueryRepository {

  @Override
  public String getAllInstructors() {
    return "SELECT i.id,concat (u.first_name, ' ', u.last_name) as fullName,i.specialization,"
        + "u.phone_number,u.email,u.password from instructors i "
        + "join users u on i.user_id=u.id order by i.id desc;";
  }
}
