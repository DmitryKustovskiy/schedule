package spring.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
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
	
	@NotBlank(message = "You should enter start time")
	private String startTime;
	
	@NotBlank(message = "You should enter end time")
	private String endTime;
	private GroupDto groupDto;
	private SubjectDto subjectDto;

}
