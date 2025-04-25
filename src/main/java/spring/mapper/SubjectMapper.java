package spring.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spring.dto.SubjectDto;
import spring.model.Subject;

public class SubjectMapper {

	public static SubjectDto toDto(Subject subject) {
		if (subject == null)
			return null;
		SubjectDto subjectDto = new SubjectDto();
		subjectDto.setId(subject.getId());
		subjectDto.setName(subject.getName());
		return subjectDto;

	}

	public static Subject toEntity(SubjectDto subjectDto) {
		if (subjectDto == null)
			return null;
		Subject subject = new Subject();
		subject.setId(subjectDto.getId());
		subject.setName(subjectDto.getName());
		return subject;

	}

	public static List<SubjectDto> toDtoList(List<Subject> subjects) {
		if (subjects == null)
			return null;
		return subjects.stream().map(SubjectMapper::toDto).toList();
	}

	public static List<Subject> toEntityList(List<SubjectDto> subjectDtos) {
		if (subjectDtos == null)
			return null;
		return subjectDtos.stream().map(SubjectMapper::toEntity).toList();
	}

}
