package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import spring.model.Group;
import spring.model.Student;

@Repository
public class StudentRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Student> findAll() {
		return entityManager.createQuery("FROM Student", Student.class).getResultList();

	}

	public Student findById(int id) {
		return entityManager.find(Student.class, id);
	}

	public Student save(Student student) {
		entityManager.persist(student);
		return student;
	}

	public Student update(Student updatedStudent) {
		entityManager.merge(updatedStudent);
		return updatedStudent;
	}

	public void setGroup(Student updatedStudent) {
		entityManager.merge(updatedStudent);
	}

	public void delete(Student studentToBeRemoved) {
		entityManager.remove(studentToBeRemoved);

	}
}
