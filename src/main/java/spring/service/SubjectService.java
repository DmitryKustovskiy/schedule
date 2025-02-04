package spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.model.Group;
import spring.model.Subject;
import spring.repository.SubjectRepository;

@Service
public class SubjectService {

	private final SubjectRepository repository;

	@Autowired
	public SubjectService(SubjectRepository repository) {
		this.repository = repository;
	}

	public List<Subject> findAll() {
		return repository.findAll();
	}

	public Subject findById(int id) {
		return repository.findById(id);
	}

	public Subject save(Subject subject) {
		return repository.save(subject);
	}

	public void update(Subject subject, int id) {
		repository.update(subject, id);
	}

	public boolean delete(int id) {
		return repository.delete(id);
	}
	
	public boolean checkIfSubjectExists(String subjectName) {
		List<Subject> allSubjects = repository.findAll();
		return allSubjects.stream().anyMatch(
				existingSubject -> existingSubject.getName().equalsIgnoreCase(subjectName));
	}

}
