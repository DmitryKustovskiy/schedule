package spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.model.Group;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
	
	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private Group group;

	@Override
	public String toString() {
		return "id = " + id + ", " + " FirstName = " + firstName + ", " + " LastName = " + lastName + ", " + 
				   " Group = " + group;
	}
	
}
