package spring.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.model.Person;
import spring.model.Role;
import spring.repository.PersonRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {

	private final PersonRepository personRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void register(String username, String rawPassword, Role role) {
		Person person = new Person();
		person.setUsername(username);
		person.setPassword(passwordEncoder.encode(rawPassword));
		person.setRole(role);
		personRepository.save(person);
	}
	
	public boolean userExists(String username) {
		return personRepository.findByUsername(username).isPresent();
	}

}
