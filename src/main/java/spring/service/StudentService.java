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
		Student student = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with this id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		});

		return studentMapper.toDto(student);

	}

	@Transactional
	public Student save(StudentDto studentDto) {
		Student student = studentMapper.toEntity(studentDto);
		studentRepository.save(student);
		log.info("Student {} was saved correctly", student.getFirstName() + " " + student.getLastName());

		return student;

	}

	@Transactional
	public Student update(StudentDto updatedStudentDto, int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		});

		student.setFirstName(updatedStudentDto.getFirstName());
		student.setLastName(updatedStudentDto.getLastName());
		Student updatedStudent = studentRepository.save(student);
		log.info("Student with id {} was updated correctly", id);

		return updatedStudent;

	}

	@Transactional
	public void setGroup(StudentDto updatedStudentDto, int id) {
		Student studentToBeUpdated = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student not found");
		});

		Group group = groupRepository.findById(updatedStudentDto.getGroupDto().getId()).orElseThrow(() -> {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Group not found");
		});

		studentToBeUpdated.setGroup(group);
		studentRepository.save(studentToBeUpdated);
		log.info("Student with id {} was updated correctly", id);

	}

	@Transactional
	public void delete(int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student was not found");
		});

		studentRepository.delete(student);
		log.info("Student with id {} was deleted correctly", id);

	}

	public boolean checkIfNull(String studentName) {
		return studentName.isEmpty();
	}
}