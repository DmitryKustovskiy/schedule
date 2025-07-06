package spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
	
	Optional<Person> findByUsername(String name);

}
