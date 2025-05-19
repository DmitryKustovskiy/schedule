package spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import spring.model.Group;
import spring.model.ScheduleItem;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Integer> {

	@Query(value = "SELECT s FROM ScheduleItem s " + "JOIN FETCH s.group g " + "JOIN FETCH s.subject sb "
			+ "WHERE g.name ILIKE %:input%")
	List<ScheduleItem> findByGroupName(@Param("input") String input);

	List<ScheduleItem> findAll();

	Optional<ScheduleItem> findById(int id);

	ScheduleItem save(ScheduleItem scheduleItem);

	void delete(ScheduleItem scheduleItem);

}
