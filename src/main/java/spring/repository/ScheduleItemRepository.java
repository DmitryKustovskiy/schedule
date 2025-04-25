package spring.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
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
	
	public ScheduleItem findByDate(LocalDate localDate) {
		 ScheduleItem singleResult = entityManager.createQuery(
				"FROM ScheduleItem s WHERE s.startTime = :startTime", ScheduleItem.class)
		 .setParameter("startTime", localDate.getYear() + "-" + localDate.getMonth() 
		 + "-" + localDate.getDayOfMonth()).getSingleResult();
		 return singleResult;
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

}
