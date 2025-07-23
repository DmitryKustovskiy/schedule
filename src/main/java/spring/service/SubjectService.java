package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.SubjectDto;
import spring.exception.EntityAlreadyExistsException;
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
		Subject subject = findSubject(id);
		return subjectMapper.toDto(subject);
	}

	@Transactional
	public Subject save(SubjectDto subjectDto) {
		if (subjectRepository.existsByNameIgnoreCase(subjectDto.getName())) {
			throw new EntityAlreadyExistsException("Subject with this name already exists");
		}

		Subject subject = subjectMapper.toEntity(subjectDto);
		Subject savedSubject = subjectRepository.save(subject);
		log.info("Subject {} was saved correctly", subject);
		return savedSubject;
	}

	@Transactional
	public Subject update(SubjectDto updatedSubjectDto, int id) {
		if (subjectRepository.existsByNameIgnoreCase(updatedSubjectDto.getName())) {
			throw new EntityAlreadyExistsException("Subject with this name already exists");
		}

		Subject subject = findSubject(id);

		if (!subject.getVersion().equals(updatedSubjectDto.getVersion())) {
			throw new OptimisticLockException();
		}
		subject.setName(updatedSubjectDto.getName());

		log.info("Group with id {} was updated correctly", id);
		return subjectRepository.save(subject);
	}

	@Transactional
	public void delete(int id) {
		Subject subject = findSubject(id);

		subjectRepository.delete(subject);
		log.info("Subject with this id {} was deleted correctly", id);
	}

	private Subject findSubject(int id) {
		return subjectRepository.findById(id).orElseThrow(() -> {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Sorry, subject was not found!");
		});
	}

}
