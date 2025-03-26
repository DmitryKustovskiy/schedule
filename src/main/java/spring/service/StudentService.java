package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import spring.dto.StudentDto;
import spring.mapper.StudentMapper;
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

	public List<StudentDto> findAll() {
		List<Student> allStudents = studentRepository.findAll();
		return StudentMapper.toDtoList(allStudents);

	}

	public StudentDto findById(int id) {
		Student existingStudent = studentRepository.findById(id);
		if (existingStudent == null) {
			log.warn("Student with this id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		}
		return StudentMapper.toDto(existingStudent);

	}

	@Transactional
	public Student save(StudentDto studentDto) {
		Student student = StudentMapper.toEntity(studentDto);
		studentRepository.save(student);
		log.info("Student {} was saved correctly", student.getFirstName() + " " + student.getLastName());
		return student;

	}

	@Transactional
	public Student update(StudentDto updatedStudentDto, int id) {
		Student existingStudent = studentRepository.findById(id);
		if (existingStudent == null) {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		}
		existingStudent.setFirstName(updatedStudentDto.getFirstName());
		existingStudent.setLastName(updatedStudentDto.getLastName());
		Student updatedStudent = studentRepository.update(existingStudent);
		log.info("Student with id {} was updated correctly", id);
		return updatedStudent;

	}

	@Transactional
	public void setGroup(StudentDto updatedStudentDto, int id) {
		Student studentToBeUpdated = studentRepository.findById(id);
		Group group = groupRepository.findById(updatedStudentDto.getGroup().getId());
		studentToBeUpdated.setGroup(group);
		studentRepository.setGroup(studentToBeUpdated);
		log.info("Student with id {} was updated correctly", id);

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