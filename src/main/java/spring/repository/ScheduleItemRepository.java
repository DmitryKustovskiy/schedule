package spring.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import spring.model.ScheduleItem;

@Repository
public class ScheduleItemRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<ScheduleItem> findAll() {
		return entityManager.createQuery("FROM ScheduleItem", ScheduleItem.class).getResultList();
	}

	public ScheduleItem findById(int id) {
		return entityManager.find(ScheduleItem.class, id);
	}

	public ScheduleItem save(ScheduleItem scheduleItem) {
		entityManager.persist(scheduleItem);
		return scheduleItem;
	}

	public ScheduleItem update(ScheduleItem updatedScheduleItem) {
		return entityManager.merge(updatedScheduleItem);

	}

	public void delete(ScheduleItem scheduleItem) {
		entityManager.remove(scheduleItem);

	}

	public void deleteScheduleByClassId(ScheduleItem scheduleItem) {
		entityManager.remove(scheduleItem);
	}

	public void deleteScheduleBySubjectId(ScheduleItem scheduleItem) {
		entityManager.remove(scheduleItem);

	}
}
