package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import spring.mapper.GroupMapper;
import spring.mapper.StudentMapper;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class StudentServiceIT {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupMapper groupMapper;

	private Student alex;

	private Student ivan;

	@BeforeEach
	void setUp() {
		saveStudents();

	}

	@Test
	void shouldFindAllStudents() {
		var actualResult = studentService.findAll();
		
		assertEquals(2, actualResult.size());
		assertThat(actualResult).extracting(StudentDto::getFirstName).containsExactlyInAnyOrder("Alex", "Ivan");
		
	}

	@Test
	void shouldFindStudentById() {
		assertThat(studentService.findById(alex.getId())).isNotNull();
		assertThat(studentService.findById(ivan.getId())).isNotNull();
		assertEquals("Alex", studentService.findById(alex.getId()).getFirstName());
		assertEquals("Ivan", studentService.findById(ivan.getId()).getFirstName());
		
	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionIfStudentNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class, 
				() -> studentService.findById(notExistedId));
		
	}

	@Test
	void shouldSaveStudent() {
		var testGroupDto = new GroupDto();
		testGroupDto.setName("TestGroup");
		var savedGroup = groupService.save(testGroupDto);
		var testStudentDto = new StudentDto();
		testStudentDto.setFirstName("TestFirstname");
		testStudentDto.setLastName("TestLastname");
		testStudentDto.setGroupDto(groupMapper.toDto(savedGroup));
		var actualResult = studentService.save(testStudentDto);
		var existedStudentDto = studentService.findById(actualResult.getId());
		var allActualStudents = studentService.findAll();
		
		assertThat(actualResult.getFirstName()).isEqualTo(existedStudentDto.getFirstName());
		assertEquals(3, allActualStudents.size());
		assertThat(allActualStudents)
		.extracting(StudentDto::getFirstName)
		.containsExactlyInAnyOrder("Alex", "Ivan",
				"TestFirstname");

	}

	@Test
	void shouldUpdateStudent() {
		var updatedStudentDto = new StudentDto();
		updatedStudentDto.setFirstName("FirstName");
		updatedStudentDto.setLastName("LastName");
		var studentToBeUpdated = studentService.findById(alex.getId());
		updatedStudentDto.setVersion(studentToBeUpdated.getVersion());
		var actualResult = studentService.update(updatedStudentDto, studentToBeUpdated.getId());
		
		assertEquals(updatedStudentDto.getFirstName(), actualResult.getFirstName());
		assertEquals(updatedStudentDto.getLastName(), actualResult.getLastName());
		
	}

	@Test
	void shouldSetGroupToStudent() {
		var oldGroupDto = new GroupDto();
		oldGroupDto.setName("OldGroup");
		var savedOldGroup = groupService.save(oldGroupDto);

		var newGroupDto = new GroupDto();
		newGroupDto.setName("NewGroup");
		var savedNewGroup = groupService.save(newGroupDto);

		var studentToBeUpdated = new StudentDto();
		studentToBeUpdated.setFirstName("FirstName");
		studentToBeUpdated.setLastName("LastName");
		studentToBeUpdated.setGroupDto(groupMapper.toDto(savedOldGroup));
		var savedToBeUpdatedStudent = studentService.save(studentToBeUpdated);

		var updatedStudent = new StudentDto();
		updatedStudent.setFirstName("FirstName");
		updatedStudent.setLastName("LastName");
		updatedStudent.setGroupDto(groupMapper.toDto(savedNewGroup));
		updatedStudent.setVersion(savedToBeUpdatedStudent.getVersion());
		studentService.setGroup(updatedStudent, savedToBeUpdatedStudent.getId());
		var existedStudentDto = studentService.findById(savedToBeUpdatedStudent.getId());

		assertEquals("NewGroup", existedStudentDto.getGroupDto().getName());

	}

	@Test
	void shouldDeleteStudent() {
		var studentDtoToBeDeleted = studentService.findById(alex.getId());
		studentService.delete(studentDtoToBeDeleted.getId());
		var allStudents = studentService.findAll();
		
		assertEquals(1, allStudents.size());
		assertThat(allStudents).extracting(StudentDto::getFirstName).doesNotContain("Alex");
		assertThat(allStudents).extracting(StudentDto::getLastName).doesNotContain("Alexeev");
		
	}

	@Test
	void studentNameShouldNotBeEmpty() {
		var name = "";
		var actualResult = studentService.checkIfNull(name);
		
		assertTrue(actualResult);
		
	}

	@Test
	void shouldThrowOptimisticLockExceptionOnUpdate() {
		var existedStudentDto = studentService.findById(alex.getId());
		existedStudentDto.setFirstName("Test");
		existedStudentDto.setVersion(existedStudentDto.getVersion() -1);
		
		assertThrows(OptimisticLockException.class, 
				() -> studentService.update(existedStudentDto, alex.getId()));
		
	}

	@Test
	void shouldThrowOptimisticLockExceptionOnSetGroup() {
		var oldGroupDto = new GroupDto();
		oldGroupDto.setName("OldGroup");
		var savedOldGroup = groupService.save(oldGroupDto);

		var newGroupDto = new GroupDto();
		newGroupDto.setName("NewGroup");
		var savedNewGroup = groupService.save(newGroupDto);

		var studentDto = new StudentDto();
		studentDto.setFirstName("TestFirstname");
		studentDto.setLastName("TestLastname");
		studentDto.setGroupDto(groupMapper.toDto(savedOldGroup));
		var savedStudent = studentService.save(studentDto);
		var existedStudent = studentService.findById(savedStudent.getId());
		var outDatedStudentDto = new StudentDto();
		outDatedStudentDto.setGroupDto(groupMapper.toDto(savedNewGroup));
		outDatedStudentDto.setVersion(existedStudent.getVersion() - 1);

		assertThrows(OptimisticLockException.class, () -> {
			studentService.setGroup(outDatedStudentDto, savedStudent.getId());
		});

	}

	private void saveStudents() {
		alex = new Student();
		alex.setFirstName("Alex");
		alex.setLastName("Alexeev");
		studentRepository.save(alex);

		ivan = new Student();
		ivan.setFirstName("Ivan");
		ivan.setLastName("Ivanov");
		studentRepository.save(ivan);

	}

}
