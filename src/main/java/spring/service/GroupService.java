package spring.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.configuration.EntityManagerUtil;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
public class GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public List<Group> findAll() {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return groupRepository.findAll(entityManager);
        }
    }

    public Group findById(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return groupRepository.findById(entityManager, id);
        }
    }

    public Group findGroupByStudentId(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            Student student = studentRepository.findById(entityManager, id);
            return groupRepository.findById(entityManager, student.getGroup().getId());
        }
    }

    public Group save(Group group) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                log.info("Transaction has begun, {}", transaction);
                groupRepository.save(entityManager, group);
                log.info("Group {} is in persistent state, {}", group, entityManager);
                transaction.commit();
                log.info("Group {} was saved correctly!", group);
                return group;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void update(Group group, int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                groupRepository.update(entityManager, group, id);
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
                groupRepository.delete(entityManager, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public boolean checkIfGroupExists(String groupName) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            List<Group> allGroups = groupRepository.findAll(entityManager);
            return (allGroups.stream().anyMatch(
                    existingGroup -> existingGroup.getName().equalsIgnoreCase(groupName)));
        }

    }

    public boolean checkIfGroupIsNotEmpty(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return studentRepository.findAll(entityManager).stream().anyMatch(student -> student.getGroup().getId() == id);
        }

    }
}
