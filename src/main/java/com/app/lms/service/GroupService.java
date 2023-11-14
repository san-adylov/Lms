package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.groupDto.GroupRequest;
import com.app.lms.dto.groupDto.GroupResponse;
import com.app.lms.dto.studentDto.StudentResponseGroup;
import java.util.List;

public interface GroupService {

    SimpleResponse saveGroup(GroupRequest groupRequest);

    List<GroupResponse> getAllGroups();

    SimpleResponse updateGroup(Long groupId, GroupRequest groupRequest);

    List<StudentResponseGroup> getGroupById(Long groupId);

    SimpleResponse deleteGroupById(Long groupId);
}
