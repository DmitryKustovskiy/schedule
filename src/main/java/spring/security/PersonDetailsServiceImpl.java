package spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import spring.model.Person;
import spring.repository.PersonRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonDetailsServiceImpl implements UserDetailsService {

	private final PersonRepository personRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person person = personRepository.findByUsername(username).orElseThrow(() -> {
			throw new UsernameNotFoundException("User was not found");
		});
		return new PersonDetails(person);
	}

}
