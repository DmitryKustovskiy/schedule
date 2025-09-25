package spring.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import spring.model.Person;
import spring.model.Role;
import spring.repository.PersonRepository;

public class RegistrationServiceUnitTest {

	private PersonRepository personRepository;
	private PasswordEncoder passwordEncoder;
	private RegistrationService registrationService;

	@BeforeEach
	void setup() {
		personRepository = mock(PersonRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		registrationService = new RegistrationService(personRepository, passwordEncoder);

	}

	@Test
	void shouldRegisterPersonWithEncodedPassword() {
		var username = "Ivan";
		var password = "123";
		var encodedPassword = "ENCODED";
		var role = Role.USER;
		when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

		registrationService.register(username, password, role);

		verify(passwordEncoder, times(1)).encode(password);
		verify(personRepository, times(1)).save(argThat(person -> person.getUsername().equals(username)
				&& person.getPassword().equals(encodedPassword) && person.getRole() == role));
		
	}
	
	@Test
	void shouldReturnTrueWheUserExists() {
		var username = "Ivan";
		var person = new Person();
		person.setUsername(username);
		
		when(personRepository.findByUsername(username)).thenReturn(Optional.of(person));
		
		boolean actualResult = registrationService.userExists(username);
		assertThat(actualResult).isTrue();
		verify(personRepository, times(1)).findByUsername(username);
		
	}
	
	@Test
	void shouldReturnFalseWhenUserDoesNotExist() {
		var username = "Ivan";
		var person = new Person();
		person.setUsername(username);
		
		when(personRepository.findByUsername("Test")).thenReturn(Optional.of(person));
		
		boolean actualResult = registrationService.userExists(username);
		assertThat(actualResult).isFalse();
		verify(personRepository, times(1)).findByUsername(username);
		
	}

}
