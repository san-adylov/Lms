package com.app.lms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.groupDto.GroupRequest;
import com.app.lms.dto.groupDto.GroupResponse;
import com.app.lms.dto.studentDto.StudentResponseGroup;
import com.app.lms.service.GroupService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Group API", description = "API for group CRUD management")
public class GroupApi {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Save group", description = "Save group")
    public SimpleResponse saveGroup(@Valid @RequestBody GroupRequest groupRequest) {
        return groupService.saveGroup(groupRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @GetMapping
    @Operation(summary = "Get all groups", description = "Get all groups")
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "Update group", description = "Update group by id ")
    public SimpleResponse updateGroup(@Valid @RequestBody GroupRequest groupRequest, @PathVariable Long groupId) {
        return groupService.updateGroup(groupId, groupRequest);
    }

    @GetMapping("/getStudents/{groupId}")
    @Operation(summary = "Get group's students", description = "Get group's students by group id ")
    public List<StudentResponseGroup> getAllGroupById(@PathVariable Long groupId) {
        return groupService.getGroupById(groupId);
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "Delete group", description = "Delete group by id")
    public SimpleResponse deleteGroup(@PathVariable Long groupId) {
        return groupService.deleteGroupById(groupId);
    }

}