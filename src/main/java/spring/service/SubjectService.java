package spring.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.configuration.EntityManagerUtil;
import spring.model.Group;
import spring.model.Subject;
import spring.repository.SubjectRepository;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return subjectRepository.findAll(entityManager);
        }
    }

    public Subject findById(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return subjectRepository.findById(entityManager, id);
        }
    }

    public Subject save(Subject subject) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                subjectRepository.save(entityManager, subject);
                transaction.commit();
                return subject;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void update(Subject subject, int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                subjectRepository.update(entityManager, subject, id);
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
                subjectRepository.delete(entityManager, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public boolean checkIfSubjectExists(String subjectName) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            List<Subject> allSubjects = subjectRepository.findAll(entityManager);
            return allSubjects.stream().anyMatch(
                    existingSubject -> existingSubject.getName().equalsIgnoreCase(subjectName));
        }
    }

}
