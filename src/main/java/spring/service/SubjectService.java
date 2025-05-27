package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.SubjectDto;
import spring.mapper.SubjectMapper;
import spring.model.Subject;
import spring.repository.SubjectRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectService {
	private final SubjectRepository subjectRepository;
	private final SubjectMapper subjectMapper;

	public List<SubjectDto> findAll() {
		List<Subject> allSubjects = subjectRepository.findAll();
		return subjectMapper.toDtoList(allSubjects);
	}

	public SubjectDto findById(int id) {
		Subject subject = subjectRepository.findById(id).orElseThrow(() -> {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		});

		return subjectMapper.toDto(subject);
	}

	@Transactional
	public Subject save(SubjectDto subjectDto) {
		Subject subject = subjectMapper.toEntity(subjectDto);
		subjectRepository.save(subject);
		log.info("Subject {} was saved correctly", subject);

		return subject;
	}

	@Transactional
	public Subject update(SubjectDto subjectDto, int id) {
		Subject subject = subjectRepository.findById(id).orElseThrow(() -> {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		});

		subject.setName(subjectDto.getName());
		log.info("Subject {} was updated correctly");

		return subjectRepository.save(subject);

	}

	@Transactional
	public void delete(int id) {
		Subject subject = subjectRepository.findById(id).orElseThrow(() -> {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		});

		log.info("Subject with this id {} was deleted correctly", id);
		subjectRepository.delete(subject);
	}

	public boolean checkIfSubjectExists(String subjectName) {
		List<Subject> allSubjects = subjectRepository.findAll();
		return allSubjects.stream()
				.anyMatch(existingSubject -> existingSubject.getName().equalsIgnoreCase(subjectName));
	}
}
