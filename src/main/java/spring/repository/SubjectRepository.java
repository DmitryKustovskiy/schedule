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

    public List<Subject> findAll(EntityManager entityManager) {
        return entityManager.createQuery("FROM Subject", Subject.class).getResultList();
    }

    public Subject findById(EntityManager entityManager, int id) {
        return entityManager.find(Subject.class, id);
    }

    public Subject save(EntityManager entityManager, Subject subject) {
        entityManager.persist(subject);
        return subject;
    }

    public void update(EntityManager entityManager, Subject updatedSubject, int id) {
        Subject subjectToBeUpdated = entityManager.find(Subject.class, id);
        if (subjectToBeUpdated != null) {
            subjectToBeUpdated.setName(updatedSubject.getName());
        }

    }

    public void delete(EntityManager entityManager, int id) {
        Subject subjectToBeRemoved = entityManager.find(Subject.class, id);
        if (subjectToBeRemoved != null) {
            entityManager.remove(subjectToBeRemoved);
        }
    }

}
