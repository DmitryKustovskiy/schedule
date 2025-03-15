package spring.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
@Transactional(readOnly = true)
public class StudentService {

	private final StudentRepository studentRepository;
	private final GroupRepository groupRepository;
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
		this.studentRepository = studentRepository;
		this.groupRepository = groupRepository;
	}

	public List<Student> findAll() {
		return studentRepository.findAll();

	}

	public Student findById(int id) {
		Student existingStudent = studentRepository.findById(id);
		if (existingStudent == null) {
			log.warn("Student with this id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		}
		return existingStudent;

	}

	@Transactional
	public Student save(Student student) {
		studentRepository.save(student);
		log.info("Student {} was saved correctly", student.getFirstName() + " " + student.getLastName());
		return student;

	}

	@Transactional
	public Student update(Student updatedStudent, int id) {
		Student existingStudent = studentRepository.findById(id);
		if (existingStudent == null) {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		}
		existingStudent.setFirstName(updatedStudent.getFirstName());
		existingStudent.setLastName(updatedStudent.getLastName());
		log.info("Student with id {} was updated correctly", id);
		return studentRepository.update(existingStudent);

	}

	@Transactional
	public void setGroup(Student updatedStudent, int id) {
		Student studentToBeUpdated = studentRepository.findById(id);
		Group group = groupRepository.findById(updatedStudent.getGroup().getId());
		studentToBeUpdated.setGroup(group);
		studentRepository.setGroup(studentToBeUpdated);

	}

	@Transactional
	public void delete(int id) {
		Student existingStudent = studentRepository.findById(id);
		if (existingStudent == null) {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student was not found");
		}
		studentRepository.delete(existingStudent);
		log.info("Student with id {} was deleted correctly", id);

	}

	public boolean checkIfNull(String studentName) {
		return studentName.isEmpty();
	}
}