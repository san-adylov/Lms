package com.app.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.app.lms.dto.groupDto.GroupResponse;
import com.app.lms.entities.Group;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT NEW com.app.lms.dto.groupDto.GroupResponse(g.id,g.groupName,g.image,g.description,g.createDate) from Group g order by g.id desc ")
    List<GroupResponse> getAllGroups();

    boolean existsByGroupName(String groupName);
}
