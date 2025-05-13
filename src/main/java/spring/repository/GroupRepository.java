package spring.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import spring.model.Group;

@Repository
public class GroupRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Group> findAll() {
		return entityManager.createQuery("FROM Group", Group.class).getResultList();
	}

	public Group findById(int id) {
		return entityManager.find(Group.class, id);
	}

	public Group save(Group group) {
		entityManager.persist(group);
		return group;
	}

	public Group update(Group updatedGroup) {
		entityManager.merge(updatedGroup);
		return updatedGroup;
	}

	public void delete(Group group) {
		entityManager.remove(group);
	}


}
