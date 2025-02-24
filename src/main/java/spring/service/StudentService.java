package spring.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.configuration.EntityManagerUtil;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return studentRepository.findAll(entityManager);
        }

    }

    public Student findById(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return studentRepository.findById(entityManager, id);
        }

    }

    public Student save(Student student) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                studentRepository.save(entityManager, student);
                transaction.commit();
                return student;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void update(Student student, int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                studentRepository.update(entityManager, student, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void setGroup(Student student, int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                studentRepository.setGroup(entityManager, student, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void delete(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                studentRepository.delete(entityManager, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}