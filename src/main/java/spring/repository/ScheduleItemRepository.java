package spring.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spring.model.ScheduleItem;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Integer> {

	@EntityGraph(attributePaths = { "group", "subject" })
	@Query(value = "SELECT s FROM ScheduleItem s "
			+ "WHERE LOWER (s.group.name) LIKE LOWER (concat ('%', :input, '%'))")
	List<ScheduleItem> findByGroupName(@Param("input") String input);
	
	@Query(value = "SELECT s FROM ScheduleItem s JOIN FETCH s.group JOIN FETCH s.subject")
	List<ScheduleItem> findAllWithDetails();

}
