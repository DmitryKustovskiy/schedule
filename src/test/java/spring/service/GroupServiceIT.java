package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import spring.dto.GroupDto;
import spring.dto.StudentDto;
import spring.exception.EntityAlreadyExistsException;
import spring.exception.GroupNotEmptyException;
import spring.mapper.GroupMapper;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GroupServiceIT {

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupMapper groupMapper;

	private Group leads;

	private Group united;

	@BeforeEach
	void setup() {
		saveTestGroups();
	}

	@Test
	void shouldFindAllGroups() {
		var actualResult = groupService.findAll();

		assertEquals(2, actualResult.size());
		assertThat(actualResult).extracting(GroupDto::getName).containsExactlyInAnyOrder("Leads", "United");

	}

	@Test
	void shouldFindGroupById() {
		assertEquals("Leads", groupService.findById(leads.getId()).getName());
		assertEquals("United", groupService.findById(united.getId()).getName());

	}
	
	@Test
	void shouldThrowEntitytNotFoundExceptionIfGroupNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class, 
				() -> groupService.findById(notExistedId));
		
	}

	@Test
	void shouldFindGroupByStudentGroupId() {
		var testStudentDto = new StudentDto();
		testStudentDto.setFirstName("FirstName");
		testStudentDto.setLastName("LastName");
		testStudentDto.setGroupDto(groupMapper.toDto(leads));
		var savedStudent = studentService.save(testStudentDto);
		var actualResult = groupService.findGroupByStudentGroupId(savedStudent.getId());

		assertEquals(leads.getName(), actualResult.getName());

	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionWhenStudentNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class,
				() -> groupService.findGroupByStudentGroupId(notExistedId));
		
	}

	@Test
	void shouldSaveGroup() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("TestGroup");
		var actualResult = groupService.save(testGroupDto);

		assertNotNull(actualResult.getId());
		assertEquals(testGroupDto.getName(), actualResult.getName());

	}
	
	@Test
	void shouldThrowEntityAlreadyExistsExceptionOnGroupSave() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("Leads");

		assertThrows(EntityAlreadyExistsException.class, () -> groupService.save(testGroupDto));

	}

	@Test
	void shouldUpdateGroup() {
		var updatedGroupDto = new GroupDto();
		updatedGroupDto.setName("TestGroup");
		var groupToBeUpdated = groupService.findById(leads.getId());
		updatedGroupDto.setVersion(groupToBeUpdated.getVersion());
		var actualResult = groupService.update(updatedGroupDto, groupToBeUpdated.getId());

		assertEquals(updatedGroupDto.getName(), actualResult.getName());

	}
	
	@Test
	void shouldThrowEntityAlreadyExistsExceptionOnGroupUpdate() {
		var updatedGroupDto = new GroupDto();
		updatedGroupDto.setName("Leads");

		assertThrows(EntityAlreadyExistsException.class, () -> groupService.update(updatedGroupDto, leads.getId()));
	
	}

	@Test
	void shouldDeleteGroup() {
		var existedGroupDto = groupService.findById(united.getId());
		groupService.delete(existedGroupDto.getId());
		var allGroups = groupService.findAll();

		assertEquals(1, allGroups.size());
		assertThat(allGroups).extracting(GroupDto::getName).doesNotContain("United");

	}

	@Test
	void shouldThrowGroupIsNotEmptyException() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("TestGroup");
		var savedGroup = groupService.save(testGroupDto);
		var testStudentDto = new StudentDto();
		testStudentDto.setFirstName("Alex");
		testStudentDto.setLastName("Alexeev");
		testStudentDto.setGroupDto(groupMapper.toDto(savedGroup));
		studentService.save(testStudentDto);

		assertThrows(GroupNotEmptyException.class, () -> groupService.delete(savedGroup.getId()));

	}

	@Test
	void shouldThrowOptimisticLockExceptionOnGroupUpdate() {
		var testGroupDto = groupService.findById(leads.getId());
		testGroupDto.setName("TestGroup");
		testGroupDto.setVersion(testGroupDto.getVersion() - 1);

		assertThrows(OptimisticLockException.class, () -> groupService.update(testGroupDto, leads.getId()));
	
	}

	private void saveTestGroups() {
		leads = groupRepository.save(new Group("Leads"));
		united = groupRepository.save(new Group("United"));
		
	}

}
