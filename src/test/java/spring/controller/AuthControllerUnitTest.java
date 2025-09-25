package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import spring.model.Role;
import spring.security.AuthController;
import spring.security.RegistrationDto;
import spring.security.RegistrationService;

public class AuthControllerUnitTest {
	
	private RegistrationService registrationService;
	private AuthController authController;
	
	@BeforeEach
	void setup() {
		registrationService = mock(RegistrationService.class);
		authController = new AuthController(registrationService);
		
	}

	@Test
	void shouldReturnLoginPageOnLogin() {
		var actualResult = authController.loginPage();
		
		assertThat(actualResult).isEqualTo("login");
		
	}
	
	@Test
	void shouldReturnRegisterPageAndSetModelOnRegister() {
		var model = new ExtendedModelMap();
		var actualResult = authController.registerPage(model);
		
		assertThat(actualResult).isEqualTo("register");
		assertThat(model.containsAttribute("registrationDto")).isTrue();
		
	}
	
	@Test
	void shouldRedirectToLoginPageOnRegisterSucceeds() {
		var registrationDto = new RegistrationDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		
		var actualResult = authController.register(registrationDto, bindingResult);
		
		assertThat(actualResult).isEqualTo("redirect:/login");
		verify(registrationService, times(1))
		.register(registrationDto.getUsername(), registrationDto.getPassword(), registrationDto.getRole());
		
	}
	
	@Test
	void shouldReturnToRegisterPageOnRegisterFailed() {
		var registrationDto = new RegistrationDto();
		var bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		
		var actualResult = authController.register(registrationDto, bindingResult);
		
		assertThat(actualResult).isEqualTo("/register");
		verify(registrationService, times(0))
		.register(registrationDto.getUsername(), registrationDto.getPassword(), registrationDto.getRole());
		
	}
	
	@Test
	void shouldReturnToRegisterPageWhenUserALreadyExists() {
		var registrationDto = new RegistrationDto();
	    registrationDto.setUsername("Ivan");
	    registrationDto.setPassword("123");
	    registrationDto.setRole(Role.USER);
	    var bindingResult = mock(BindingResult.class);
	    
	    when(registrationService.userExists("Ivan")).thenReturn(true);
	    when(bindingResult.hasErrors()).thenReturn(true);

	    var actualResult = authController.register(registrationDto, bindingResult);

	    assertThat(actualResult).isEqualTo("/register");
	    verify(bindingResult).rejectValue("username", "error.username", "User with this login already exists");
	    verify(registrationService, times(0)).register(any(), any(), any());
		
	}
	
}
