package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import spring.dto.SubjectDto;
import spring.model.Subject;
import spring.repository.SubjectRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SubjectServiceIT {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectRepository subjectRepository;

	private Subject math;

	private Subject history;

	@BeforeEach
	void setup() {
		saveTestSubjects();
	}

	@Test
	void shouldFindAllSubjects() {
		List<SubjectDto> expectedSubjects = subjectService.findAll();

		assertEquals(2, expectedSubjects.size());
		assertThat(expectedSubjects).extracting(SubjectDto::getName)
		.containsExactlyInAnyOrder("Math", "History");

	}

	@Test
	void shouldFindSubjectById() {
		assertThat(subjectService.findById(math.getId())).isNotNull();
		assertThat(subjectService.findById(history.getId())).isNotNull();
		assertEquals("Math", subjectService.findById(math.getId()).getName());
		assertEquals("History", subjectService.findById(history.getId()).getName());

	}

	@Test
	void shouldSaveSubject() {
		SubjectDto subjectDto = new SubjectDto();
		subjectDto.setName("TestSubject");
		Subject expectedSubject = subjectService.save(subjectDto);

		assertNotNull(expectedSubject.getId());
		assertEquals("TestSubject", expectedSubject.getName());

	}

	@Test
	void shouldUpdateSubject() {
		SubjectDto testSubject = new SubjectDto();
		testSubject.setName("TestSubject");

		SubjectDto groupToBeUpdated = subjectService.findById(math.getId());
		Subject expectedGroup = subjectService.update(testSubject, groupToBeUpdated.getId());

		assertEquals("TestSubject", expectedGroup.getName());
	}

	@Test
	void shouldDeleteSubject() {
		SubjectDto mathSubject = subjectService.findById(math.getId());
		subjectService.delete(mathSubject.getId());
		List<SubjectDto> allSubjects = subjectService.findAll();

		assertEquals(1, allSubjects.size());
		assertThat(allSubjects).extracting(SubjectDto::getName).doesNotContain("Math");

	}

	@Test
	void shouldReturnTrueIfSubjectExists() {
		boolean expectedResult = subjectService.checkIfSubjectExists("Math");
		assertTrue(expectedResult);

	}

	@Test
	void shouldReturnFalseIfSubjectDoesNotExist() {
		boolean expectedResult = subjectService.checkIfSubjectExists("History Of Spain");
		assertFalse(expectedResult);

	}

	private void saveTestSubjects() {
		math = subjectRepository.save(new Subject("Math"));
		history = subjectRepository.save(new Subject("History"));
	}

}
