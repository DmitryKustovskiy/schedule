package spring.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

	private Integer id;

	@NotBlank(message = "Please, enter first name")
	@Size(min = 2, message = "Minimum 3 symbols required the firstname")
	private String firstName;

	@NotBlank(message = "Please, enter second name")
	@Size(min = 2, message = "Minimum 3 symbols required for the lastname")
	private String lastName;

	private GroupDto groupDto;
	
	private Integer version;

}
