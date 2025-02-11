package spring.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@Component
public class Student {
	private int id;
	private String firstName;
	private String lastName;
	private int classId;
	private Group group;

	public Student(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "id = " + id + ", " + " FirstName = " + firstName + ", " + " LastName = " + lastName + ", " + 
			   " Group = " + classId;
	}

}
