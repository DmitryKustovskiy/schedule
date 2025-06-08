package spring.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import spring.dto.StudentDto;
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
	private GroupRepository groupRepository;
	@Autowired
	private StudentService studentService;
	@Autowired
	private StudentMapper studentMapper;

	private Student alex;

	private Student ivan;

	@BeforeEach
	void setUp() {
		saveStudents();

	}

	@Test
	void shouldFindAllStudents() {
		List<StudentDto> actualResult = studentService.findAll();
		assertEquals(2, actualResult.size());

		assertThat(actualResult).extracting(StudentDto::getFirstName).containsExactlyInAnyOrder("Alex", "Ivan");
	}

	@Test
	void shouldFindStudentById() {
		assertThat(studentService.findById(alex.getId())).isNotNull();
		assertThat(studentService.findById(ivan.getId())).isNotNull();

		assertEquals("Alex", studentService.findById(alex.getId()).getFirstName());
		assertEquals("Alexeev", studentService.findById(alex.getId()).getLastName());

		assertEquals("Ivan", studentService.findById(ivan.getId()).getFirstName());
		assertEquals("Ivanov", studentService.findById(ivan.getId()).getLastName());
	}

	@Test
	void shouldUpdateStudent() {
		var testStudentDto = new StudentDto();
		testStudentDto.setFirstName("FirstName");
		testStudentDto.setLastName("LastName");

		var studentToBeUpdated = studentService.findById(alex.getId());

		Student expectedStudent = studentService.update(testStudentDto, studentToBeUpdated.getId());
		assertEquals("FirstName", expectedStudent.getFirstName());
		assertEquals("LastName", expectedStudent.getLastName());
	}

	@Test
	void shouldSetGroupToStudent() {
		var oldGroup = groupRepository.save(new Group("OldGroup"));
		var newGroup = groupRepository.save(new Group("NewGroup"));

		var studentToBeUpdated = new Student();
		studentToBeUpdated.setFirstName("FirstName");
		studentToBeUpdated.setLastName("LastName");
		studentToBeUpdated.setGroup(oldGroup);
		studentRepository.save(studentToBeUpdated);

		var updatedStudent = new Student();
		updatedStudent.setFirstName("FirstName");
		updatedStudent.setLastName("LastName");
		updatedStudent.setGroup(newGroup);

		studentService.setGroup(studentMapper.toDto(updatedStudent), studentToBeUpdated.getId());

		assertEquals("NewGroup", studentToBeUpdated.getGroup().getName());

	}

	@Test
	void shouldDeleteStudent() {
		var studentDtoToBeDeleted = studentService.findById(alex.getId());
		studentService.delete(studentDtoToBeDeleted.getId());

		List<StudentDto> allStudentDtos = studentService.findAll();
		assertEquals(1, allStudentDtos.size());
		assertThat(allStudentDtos).extracting(StudentDto::getFirstName).doesNotContain("Alex");
		assertThat(allStudentDtos).extracting(StudentDto::getLastName).doesNotContain("Alexeev");
	}

	@Test
	void studentNameShouldBeEmpty() {
		String name = "";
		boolean emptyName = studentService.checkIfNull(name);
		assertTrue(emptyName);
	}

	public void saveStudents() {
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
