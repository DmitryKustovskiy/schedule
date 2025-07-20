package spring.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.dto.StudentDto;
import spring.model.Student;

@Component
@RequiredArgsConstructor
public class StudentMapper implements Mapper<StudentDto, Student> {
	private final GroupMapper groupMapper;

	@Override
	public StudentDto toDto(Student student) {
		if (student == null)
			return null;
		StudentDto studentDto = new StudentDto();
		studentDto.setId(student.getId());
		studentDto.setFirstName(student.getFirstName());
		studentDto.setLastName(student.getLastName());
		studentDto.setGroupDto(groupMapper.toDto(student.getGroup()));
		studentDto.setVersion(student.getVersion());
		return studentDto;
	}

	@Override
	public Student toEntity(StudentDto studentDto) {
		if (studentDto == null)
			return null;
		Student student = new Student();
		student.setId(studentDto.getId());
		student.setFirstName(studentDto.getFirstName());
		student.setLastName(studentDto.getLastName());
		student.setVersion(studentDto.getVersion());
		return student;
	}

}
