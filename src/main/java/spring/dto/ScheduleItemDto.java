package spring.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.model.Group;
import spring.model.Subject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemDto {
	private int id;
	private String startTime;
	private String endTime;
	private GroupDto groupDto;
	private SubjectDto subjectDto;

}
