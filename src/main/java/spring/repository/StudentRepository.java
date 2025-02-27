package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import spring.model.Group;
import spring.model.Student;

@Repository
public class StudentRepository {

    public List<Student> findAll(EntityManager entityManager) {
        return entityManager.createQuery("FROM Student", Student.class).getResultList();

    }

    public Student findById(EntityManager entityManager, int id) {
        return entityManager.find(Student.class, id);
    }

    public Student save(EntityManager entityManager, Student student) {
        entityManager.persist(student);
        return student;
    }

    public void update(EntityManager entityManager, Student updatedStudent, int id) {
        Student studentTobeUpdated = entityManager.find(Student.class, id);
        if (studentTobeUpdated != null) {
            studentTobeUpdated.setFirstName(updatedStudent.getFirstName());
            studentTobeUpdated.setLastName(updatedStudent.getLastName());
        }

    }

    public void setGroup(EntityManager entityManager, Student updatedStudent, int id) {
        Student studentTobeUpdated = entityManager.find(Student.class, id);
        if (studentTobeUpdated != null && updatedStudent.getGroup() != null) {
            Group group = entityManager.find(Group.class, updatedStudent.getGroup().getId());
            studentTobeUpdated.setGroup(group);
        }

    }

    public void delete(EntityManager entityManager, int id) {
        Student studentToBeRemoved = entityManager.find(Student.class, id);
        if (studentToBeRemoved != null) {
            entityManager.remove(studentToBeRemoved);
        }
    }
}

