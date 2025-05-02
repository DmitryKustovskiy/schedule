package spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
	
	private int id;
	
	@NotBlank(message = "Please, enter first name")
	private String firstName;
	
	@NotBlank(message = "Please, enter second name")
	private String lastName;
	
	@NotNull(message = "Enter the group, please")
	@Valid
	private GroupDto groupDto;
	
}
