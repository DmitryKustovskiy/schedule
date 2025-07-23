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
import spring.dto.GroupDto;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GroupServiceIT {
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	StudentRepository studentRepository;

	private Group leads;

	private Group united;

	@BeforeEach
	void setup() {
		saveTestGroups();
	}

	@Test
	void shouldFindAllGroups() {
		List<GroupDto> expectedGroups = groupService.findAll();

		assertEquals(2, expectedGroups.size());
		assertThat(expectedGroups).extracting(GroupDto::getName).containsExactlyInAnyOrder("Leads", "United");

	}

	@Test
	void shouldFindGroupById() {
		assertEquals("Leads", groupService.findById(leads.getId()).getName());
		assertEquals("United", groupService.findById(united.getId()).getName());

	}
	
	@Test
	void findGroupByStudentId() {
		Group expectedGroup = new Group();
		expectedGroup.setName("Leads");
		
		Student testStudent = new Student();
		testStudent.setFirstName("FirstName");		
		testStudent.setLastName("LastName");
		testStudent.setGroup(leads);
		studentRepository.save(testStudent);
		
		Student student = studentRepository.findById(testStudent.getId()).get();
		Group actualGroup = groupRepository.findById(student.getGroup().getId()).get();
		
		assertEquals(expectedGroup.getName(), actualGroup.getName());
		
		
	}

	@Test
	void shouldThrowExceptionIfGroupNotFound() {
		int nonexistentId = Integer.MAX_VALUE;
		assertThrows(EntityNotFoundException.class, () -> groupService.findById(nonexistentId));

	}

	@Test
	void shouldSaveGroup() {
		GroupDto groupDto = new GroupDto();
		groupDto.setName("TestGroup");
		Group expectedGroup = groupService.save(groupDto);

		assertNotNull(expectedGroup.getId());
		assertEquals("TestGroup", expectedGroup.getName());

	}

	@Test
	void shouldUpdateGroup() {
		GroupDto testGroup = new GroupDto();
		testGroup.setName("TestGroup");
		
		GroupDto groupToBeUpdated = groupService.findById(leads.getId());
		Group expectedGroup = groupService.update(testGroup, groupToBeUpdated.getId());

		assertEquals("TestGroup", expectedGroup.getName());

	}

	@Test
	void shouldDeleteGroup() {
		GroupDto unitedGroup = groupService.findById(united.getId());
		groupService.delete(unitedGroup.getId());
		List<GroupDto> allGroups = groupService.findAll();

		assertEquals(1, allGroups.size());
		assertThat(allGroups).extracting(GroupDto::getName).doesNotContain("United");

	}

	private void saveTestGroups() {
		leads = groupRepository.save(new Group("Leads"));
		united = groupRepository.save(new Group("United"));
	}

}
