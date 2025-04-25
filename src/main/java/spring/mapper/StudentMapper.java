package spring.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spring.dto.GroupDto;
import spring.dto.StudentDto;
import spring.model.Student;

public class StudentMapper {

	public static StudentDto toDto(Student student) {
		if (student == null)
			return null;
		StudentDto studentDto = new StudentDto();
		studentDto.setId(student.getId());
		studentDto.setFirstName(student.getFirstName());
		studentDto.setLastName(student.getLastName());
		studentDto.setGroupDto(GroupMapper.toDto(student.getGroup()));
		return studentDto;
	}

	public static Student toEntity(StudentDto studentDto) {
		if (studentDto == null)
			return null;
		Student student = new Student();
		student.setId(studentDto.getId());
		student.setFirstName(studentDto.getFirstName());
		student.setLastName(studentDto.getLastName());
		student.setGroup(GroupMapper.toEntity(studentDto.getGroupDto()));
		return student;

	}

	public static List<StudentDto> toDtoList(List<Student> students) {
		if (students == null)
			return null;
		return students.stream().map(StudentMapper::toDto).toList();
	}

	public static List<Student> toEntityList(List<StudentDto> studentDtos) {
		if (studentDtos == null)
			return null;
		return studentDtos.stream().map(StudentMapper::toEntity).toList();
	}

}
