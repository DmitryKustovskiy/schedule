package spring.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class ScheduleItem {
	private int id;
	private int classId;
	private Group group;
	private int subjectId;
	private Subject subject;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public ScheduleItem(Group group, Subject subject, LocalDateTime startTime, LocalDateTime endTime) {
		this.group = group;
		this.subject = subject;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Schedule for group " + classId + ". The subject is " + subjectId +
				". Started at: " + startTime + ". " + "End at: " + endTime;
	}

}
