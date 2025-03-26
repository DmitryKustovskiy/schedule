package spring.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name = "schedule")
public class ScheduleItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "start_time")
	private LocalDateTime startTime;
	
	@Column(name = "end_time")
	private LocalDateTime endTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id")
	private Group group;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private Subject subject;

	public ScheduleItem(Group group, Subject subject, LocalDateTime startTime, LocalDateTime endTime) {
		this.group = group;
		this.subject = subject;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Schedule for group " + group.getName() + ". The subject is " + subject.getName() + ". Started at: "
				+ startTime + ". " + "End at: " + endTime;
	}

}
