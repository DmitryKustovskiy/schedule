package spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	List<Student> findAll();

	Optional<Student> findById(Integer id);

	Student save(Student student);

	void delete(Student studentToBeRemoved);

}
