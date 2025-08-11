package spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "class")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ScheduleItem> scheduleItems;
	
	@Version
	@Column(name = "version")
	private Integer version;

	public Group(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        return "Group{name='" + name + "'}";
    }

}
