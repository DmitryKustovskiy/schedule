package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import spring.dto.SubjectDto;
import spring.mapper.SubjectMapper;
import spring.model.Subject;
import spring.repository.SubjectRepository;

@Service
@Transactional(readOnly = true)
public class SubjectService {
	private final SubjectRepository subjectRepository;
	private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

	@Autowired
	public SubjectService(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	public List<SubjectDto> findAll() {
		List<Subject> allSubjects = subjectRepository.findAll();
		return SubjectMapper.toDtoList(allSubjects);
	}

	public SubjectDto findById(int id) {
		Subject existingSubject = subjectRepository.findById(id);
		if (existingSubject == null) {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		}
		return SubjectMapper.toDto(existingSubject);
	}

	@Transactional
	public Subject save(SubjectDto subjectDto) {
		Subject subject = SubjectMapper.toEntity(subjectDto);
		subjectRepository.save(subject);
		log.info("Subject {} was saved correctly", subject);
		return subject;
	}

	@Transactional
	public Subject update(SubjectDto subjectDto, int id) {
		Subject existingSubject = subjectRepository.findById(id);
		if (existingSubject == null) {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		}
		existingSubject.setName(subjectDto.getName());
		log.info("Subject {} was updated correctly");
		return subjectRepository.update(existingSubject);

	}

	@Transactional
	public void delete(int id) {
		Subject existingSubject = subjectRepository.findById(id);
		if (existingSubject == null) {
			log.warn("Subject with this id {} was not found", id);
			throw new EntityNotFoundException("Subject not found");
		}
		log.info("Subject with this id {} was deleted correctly", id);
		subjectRepository.delete(existingSubject);
	}

	public boolean checkIfSubjectExists(String subjectName) {
		List<Subject> allSubjects = subjectRepository.findAll();
		return allSubjects.stream()
				.anyMatch(existingSubject -> existingSubject.getName().equalsIgnoreCase(subjectName));
	}
}
