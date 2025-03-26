package spring.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id")
	private Group group;

	public Student(String firstName, String lastName, Group group) {
		this.group=group;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "id = " + id + ", " + " FirstName = " + firstName + ", " + " LastName = " + lastName + ", " + 
			   " Group = " + group;
	}

}
