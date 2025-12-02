package spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.StudentDto;
import spring.dto.StudentUpdateDto;
import spring.mapper.StudentMapper;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;
	private final GroupRepository groupRepository;
	private final StudentMapper studentMapper;

	public List<StudentDto> findAll() {
		List<Student> allStudents = studentRepository.findAll();
		return studentMapper.toDtoList(allStudents);
	}

	public StudentDto findById(int id) {
		Student student = findStudent(id);
		return studentMapper.toDto(student);
	}

	@Transactional
	public Student save(StudentDto studentDto) {
		Group group = groupRepository.findById(studentDto.getGroupDto().getId()).get();

		Student student = studentMapper.toEntity(studentDto);
		student.setGroup(group);

		Student savedStudent = studentRepository.save(student);
		log.info("Student {} was saved correctly", student.getFirstName() + " " + student.getLastName());
		return savedStudent;
		
	}

	@Transactional
	public Student update(StudentUpdateDto updatedStudentDto, int id) {
		Student student = findStudent(id);

		if (!student.getVersion().equals(updatedStudentDto.getVersion())) {
			throw new OptimisticLockException();
		}
		
		student.setFirstName(updatedStudentDto.getFirstName());
		student.setLastName(updatedStudentDto.getLastName());

		log.info("Student with id {} was updated correctly", id);
		Student saved = studentRepository.save(student);
		log.info("Saved student: {}", saved);
		
		return saved;
		
	}

	@Transactional
	public void setGroup(StudentDto updatedStudentDto, int id) {
		Student student = findStudent(id);
		Group group = groupRepository.findById(updatedStudentDto.getGroupDto().getId()).get();
		
		if (!student.getVersion().equals(updatedStudentDto.getVersion())) {
			throw new OptimisticLockException();
		}
		
		student.setGroup(group);
		studentRepository.save(student);
		log.info("Student with id {} was updated correctly", id);
		
	}

	@Transactional
	public void delete(int id) {
		Student student = findStudent(id);

		studentRepository.delete(student);
		log.info("Student with id {} was deleted correctly", id);
	}

	public boolean checkIfNull(String studentName) {
		return studentName.isEmpty();
	}

	private Student findStudent(int id) {
		return studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Sorry, student was not found!");
		});
	}
}