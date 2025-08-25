package spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import spring.dto.SubjectDto;
import spring.exception.EntityAlreadyExistsException;
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
		var actualResult = subjectService.findAll();

		assertEquals(2, actualResult.size());
		assertThat(actualResult).extracting(SubjectDto::getName).containsExactlyInAnyOrder("Math", "History");

	}

	@Test
	void shouldFindSubjectById() {
		assertThat(subjectService.findById(math.getId())).isNotNull();
		assertThat(subjectService.findById(history.getId())).isNotNull();
		assertEquals("Math", subjectService.findById(math.getId()).getName());
		assertEquals("History", subjectService.findById(history.getId()).getName());

	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionIfSubjectNotFound() {
		int notExistedId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundException.class, 
				() -> subjectService.findById(notExistedId));
		
	}

	@Test
	void shouldSaveSubject() {
		var testSubjectDto = new SubjectDto();
		testSubjectDto.setName("TestSubject");
		var actualResult = subjectService.save(testSubjectDto);

		assertNotNull(actualResult.getId());
		assertEquals(testSubjectDto.getName(), actualResult.getName());

	}
	
	@Test
	void shouldThrowEntityAlreadyExistsExceptionOnSubjectSave() {
		var testSubjectDto = new SubjectDto();
		testSubjectDto.setName("Math");

		assertThrows(EntityAlreadyExistsException.class, 
				() -> subjectService.save(testSubjectDto));

	}

	@Test
	void shouldUpdateSubject() {
		var testSubjectDto = new SubjectDto();
		testSubjectDto.setName("TestSubject");
		var mathSubject = subjectService.findById(math.getId());
		testSubjectDto.setVersion(mathSubject.getVersion());
		var actualResult = subjectService.update(testSubjectDto, mathSubject.getId());

		assertEquals(testSubjectDto.getName(), actualResult.getName());

	}
	
	@Test
	void shouldThrowEntityAlreadyExistsExceptionOnSubjectUpdate() {
		var updatedSubjectDto = new SubjectDto();
		updatedSubjectDto.setName("Math");

		assertThrows(EntityAlreadyExistsException.class, () -> subjectService.update(updatedSubjectDto, math.getId()));

	}

	@Test
	void shouldThrowOptimisticLockExceptionOnSubjectUpdate() {
		var testSubjectDto = subjectService.findById(math.getId());
		testSubjectDto.setName("Japanese");
		testSubjectDto.setVersion(testSubjectDto.getVersion() -1);

		assertThrows(OptimisticLockException.class, () -> subjectService.update(testSubjectDto, math.getId()));
	
	}
	
	@Test
	void shouldDeleteSubject() {
		var existedSubjectDto = subjectService.findById(math.getId());
		subjectService.delete(existedSubjectDto.getId());
		var allSubjects = subjectService.findAll();

		assertEquals(1, allSubjects.size());
		assertThat(allSubjects).extracting(SubjectDto::getName).doesNotContain("Math");

	}

	private void saveTestSubjects() {
		math = subjectRepository.save(new Subject("Math"));
		history = subjectRepository.save(new Subject("History"));
		
	}

}
