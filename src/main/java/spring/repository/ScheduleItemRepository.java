package spring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.model.Subject;

@Repository
public class ScheduleItemRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<ScheduleItem> findByGroupName(String input) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ScheduleItem> query = cb.createQuery(ScheduleItem.class);
		Root<ScheduleItem> scheduleItem = query.from(ScheduleItem.class);
		Fetch<ScheduleItem, Group> groupFetch = scheduleItem.fetch("group", JoinType.INNER);
		scheduleItem.fetch("subject", JoinType.INNER);
		Join<ScheduleItem, Group> groupJoin = (Join<ScheduleItem, Group>) groupFetch;
		query.select(scheduleItem).where(cb.like(cb.lower(groupJoin.get("name")), "%" + input.toLowerCase() + "%"));

		return entityManager.createQuery(query).getResultList();

	}

	public List<ScheduleItem> findAll() {
		return entityManager.createQuery("FROM ScheduleItem", ScheduleItem.class).getResultList();
	}

	public ScheduleItem findById(int id) {
		return entityManager.find(ScheduleItem.class, id);
	}

	public ScheduleItem findByDate(LocalDate localDate) {
		ScheduleItem singleResult = entityManager
				.createQuery("FROM ScheduleItem s WHERE s.startTime = :startTime", ScheduleItem.class)
				.setParameter("startTime",
						localDate.getYear() + "-" + localDate.getMonth() + "-" + localDate.getDayOfMonth())
				.getSingleResult();
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
