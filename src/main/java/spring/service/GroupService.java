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
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, StudentService studentService, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentService = studentService;
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
            Student student = studentService.findById(id);
            return groupRepository.findById(entityManager, student.getGroup().getId());
        }
    }

    public Group save(Group group) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                groupRepository.save(entityManager, group);
                transaction.commit();
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
