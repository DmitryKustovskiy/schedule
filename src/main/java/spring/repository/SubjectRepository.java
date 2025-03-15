package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import spring.model.Group;
import spring.model.Subject;

@Repository
public class SubjectRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Subject> findAll() {
		return entityManager.createQuery("FROM Subject", Subject.class).getResultList();
	}

	public Subject findById(int id) {
		return entityManager.find(Subject.class, id);
	}

	public Subject save(Subject subject) {
		entityManager.persist(subject);
		return subject;
	}

	public Subject update(Subject updatedSubject) {
		entityManager.merge(updatedSubject);
		return updatedSubject;
	}

	public void delete(Subject subject) {
		entityManager.remove(subject);
	}

}
