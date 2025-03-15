package spring.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name = "subject")
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "subject", cascade = CascadeType.REMOVE)
	private List<ScheduleItem> scheduleItems;
	
	public Subject(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
