package com.app.lms.repositories;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.groupDto.GroupRequest;
import com.app.lms.dto.groupDto.GroupResponse;
import com.app.lms.dto.studentDto.StudentResponseGroup;
import com.app.lms.entities.Group;
import com.app.lms.entities.Student;
import com.app.lms.entities.User;
import com.app.lms.enums.Role;
import com.app.lms.service.serviceImpl.GroupServiceImpl;
import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@RequiredArgsConstructor
public class GroupRepositoryTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(groupRepository);
    }

    @Test
    public void testSaveGroup() {
        GroupRequest groupRequest1 = new GroupRequest();
        Group groupRequest = new Group();
        groupRequest.setId(1L);
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setImage("image");
        when(groupRepository.save(any(Group.class))).thenReturn(groupRequest);
        SimpleResponse response = groupService.saveGroup(groupRequest1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Group successfully saved", response.getMessage());
        Mockito.verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    public void testUpdateGroup() {
        long groupId = 25;
        GroupRequest updateRequest = new GroupRequest();
        updateRequest.setGroupName("Updated Group");
        updateRequest.setDescription("Updated description");
        updateRequest.setImage("updated-image.jpg");
        updateRequest.setDateOfGraduation(LocalDate.of(2023, 12, 12));
        Group existingGroup = new Group();
        existingGroup.setId(groupId);
        existingGroup.setGroupName("Original Group");
        existingGroup.setDescription("Original description");
        existingGroup.setImage("original-image.jpg");
        existingGroup.setDateOfGraduate(LocalDate.of(2023, 12, 12));
        Mockito.when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(existingGroup));
        SimpleResponse response = groupService.updateGroup(groupId, updateRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Successfully Updated!", response.getMessage());
        Assertions.assertEquals(updateRequest.getGroupName(), existingGroup.getGroupName());
        Assertions.assertEquals(updateRequest.getDescription(), existingGroup.getDescription());
        Assertions.assertEquals(updateRequest.getImage(), existingGroup.getImage());
        Assertions.assertEquals(updateRequest.getDateOfGraduation(), existingGroup.getDateOfGraduate());
        Mockito.verify(groupRepository).findById(groupId);
        Mockito.verify(groupRepository).save(existingGroup);
    }

    @Test
    public void testGetAllGroup() {
        List<GroupResponse> result = groupService.getAllGroups();
        int expectedSize = 0;
        assertEquals("List of groups successfully received", expectedSize, result.size());
        verify(groupRepository).getAllGroups();
    }


    @Test
    void testDeleteGroup() {
        long groupId = 1;
        Group group = new Group();
        group.setId(groupId);
        group.setGroupName("groupName");
        group.setDescription("description");
        group.setImage("image");
        group.setDateOfGraduate(LocalDate.now());
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        SimpleResponse simpleResponse = groupService.deleteGroupById(groupId);
        Assertions.assertEquals(HttpStatus.OK, simpleResponse.getStatus());
        Assertions.assertEquals("Group with id: " + groupId + " has been deleted successfully.", simpleResponse.getMessage());
        verify(groupRepository).deleteById(groupId);
    }

    @Test
    public void testGetGroupById() {
        long groupId = 1;
        long studentId = 1;
        long userId = 1;
        Group group = new Group();
        group.setId(groupId);
        User user = new User();
        user.setId(userId);
        user.setFirstName("Baytik");
        user.setLastName("Taalaybekov");
        user.setRole(Role.STUDENT);
        user.setEmail("baytik@gmail.com");
        user.setPassword("student123"); 
        user.setPhoneNumber("0987654321");
        Student student = new Student();
        student.setId(studentId);
        student.setGroup(group);
        student.setUser(user);
        when(studentRepository.findByGroupId(groupId)).thenReturn(Collections.singletonList(student));
        List<StudentResponseGroup> studentResponses = groupService.getGroupById(groupId);
        Assertions.assertEquals(studentResponses, studentResponses);
        verify(studentRepository, times(1)).findByGroupId(groupId);
    }
}