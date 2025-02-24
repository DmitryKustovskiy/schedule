package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import spring.configuration.EntityManagerUtil;
import spring.model.Group;
import spring.model.Student;

@Repository
public class GroupRepository {

    public List<Group> findAll(EntityManager entityManager) {
        return entityManager.createQuery("FROM Group", Group.class).getResultList();
    }

    public Group findById(EntityManager entityManager, int id) {
        return entityManager.find(Group.class, id);
    }

    public Group save(EntityManager entityManager, Group group) {
        entityManager.persist(group);
        return group;
    }


    public void update(EntityManager entityManager, Group updatedGroup, int id) {
        Group groupTobeUpdated = entityManager.find(Group.class, id);
        if (groupTobeUpdated != null) {
            groupTobeUpdated.setName(updatedGroup.getName());
        }
    }


    public void delete(EntityManager entityManager, int id) {
        Group groupToBeRemoved = entityManager.find(Group.class, id);
        if (groupToBeRemoved != null) {
            entityManager.remove(groupToBeRemoved);
        }
    }

}
