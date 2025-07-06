package spring.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import spring.model.Role;

@Data
public class RegistrationDto {
	
	@NotBlank(message = "Login is required")
	private String username;
	
	@NotBlank(message = "You should enter password")
	private String password;
	
	private Role role;

}
