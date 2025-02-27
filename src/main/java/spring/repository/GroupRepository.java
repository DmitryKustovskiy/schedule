package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import spring.model.Group;

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
