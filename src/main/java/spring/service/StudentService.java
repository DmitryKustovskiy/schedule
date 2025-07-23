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
import spring.dto.StudentDto;
import spring.mapper.StudentMapper;
import spring.mapper.SubjectMapper;
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
		Group group = groupRepository.findById(studentDto.getGroupDto().getId())
				.orElseThrow(() -> new EntityNotFoundException("Group was not found"));

		Student student = studentMapper.toEntity(studentDto);
		student.setGroup(group);

		Student savedStudent = studentRepository.save(student);
		System.out.println(savedStudent.getGroup().getName());
		log.info("Student {} was saved correctly", student.getFirstName() + " " + student.getLastName());
		return savedStudent;
	}

	@Transactional
	public Student update(StudentDto updatedStudentDto, int id) {
		Student student = findStudent(id);

		if (!student.getVersion().equals(updatedStudentDto.getVersion())) {
			throw new OptimisticLockException();
		}
		student.setFirstName(updatedStudentDto.getFirstName());
		student.setLastName(updatedStudentDto.getLastName());

		log.info("Student with id {} was updated correctly", id);
		return studentRepository.save(student);
	}

	@Transactional
	public void setGroup(StudentDto updatedStudentDto, int id) {
		Student student = findStudent(id);

		Group group = groupRepository.findById(updatedStudentDto.getGroupDto().getId()).orElseThrow(() -> {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Group not found");
		});
		
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