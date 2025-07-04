package spring.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spring.dto.SubjectDto;
import spring.model.Subject;

@Component
public class SubjectMapper implements Mapper<SubjectDto, Subject> {

	@Override
	public SubjectDto toDto(Subject subject) {
		if (subject == null)
			return null;
		SubjectDto subjectDto = new SubjectDto();
		subjectDto.setId(subject.getId());
		subjectDto.setName(subject.getName());
		return subjectDto;
	}

	@Override
	public Subject toEntity(SubjectDto subjectDto) {
		if (subjectDto == null)
			return null;
		Subject subject = new Subject();
		subject.setId(subjectDto.getId());
		subject.setName(subjectDto.getName());
		return subject;
	}

}
