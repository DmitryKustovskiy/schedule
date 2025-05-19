package spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	List<Subject> findAll();

	Optional<Subject> findById(int id);

	Subject save(Subject subject);

	void delete(Subject subject);

}
