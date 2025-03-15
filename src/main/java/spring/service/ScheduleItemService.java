package spring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hibernate.annotations.SortNatural;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OrderBy;
import spring.model.ScheduleItem;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
import spring.repository.SubjectRepository;

@Service
@Transactional(readOnly = true)
public class ScheduleItemService {
	private final ScheduleItemRepository scheduleRepository;
	private final GroupRepository groupRepository;
	private final SubjectRepository subjectRepository;
	private static final Logger log = LoggerFactory.getLogger(ScheduleItemService.class);

	@Autowired
	public ScheduleItemService(ScheduleItemRepository scheduleRepository, GroupRepository groupRepository,
			SubjectRepository subjectRepository) {
		this.scheduleRepository = scheduleRepository;
		this.groupRepository = groupRepository;
		this.subjectRepository = subjectRepository;
	}

	public List<ScheduleItem> findAll() {
		return scheduleRepository.findAll();
	}

	public Set<LocalDate> findAllUniqueDates() {
		List<ScheduleItem> schedules = scheduleRepository.findAll();
		return schedules.stream().map(schedule -> schedule.getStartTime().toLocalDate()).collect(Collectors.toSet());
	}

	@Transactional
	public List<ScheduleItem> findAllWithDetails() {
		List<ScheduleItem> schedules = scheduleRepository.findAll();
		for (ScheduleItem schedule : schedules) {
			schedule.setGroup(groupRepository.findById(schedule.getGroup().getId()));
			schedule.setSubject(subjectRepository.findById(schedule.getSubject().getId()));
		}
		return schedules;
	}

	public ScheduleItem findById(int id) {
		ScheduleItem existingSchedule = scheduleRepository.findById(id);
		if (existingSchedule == null) {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		}
		return existingSchedule;
	}

	@Transactional
	public ScheduleItem save(ScheduleItem scheduleItem) {
		scheduleRepository.save(scheduleItem);
		log.info("Schedule {} was saved correctly", scheduleItem);
		return scheduleItem;

	}

	@Transactional
	public ScheduleItem update(ScheduleItem updatedScheduleItem, int id) {
		ScheduleItem scheduleItemToBeUpdated = scheduleRepository.findById(id);
		if (scheduleItemToBeUpdated == null) {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		}
		scheduleItemToBeUpdated.setGroup(updatedScheduleItem.getGroup());
		scheduleItemToBeUpdated.setSubject(updatedScheduleItem.getSubject());
		scheduleItemToBeUpdated.setStartTime(updatedScheduleItem.getStartTime());
		scheduleItemToBeUpdated.setEndTime(updatedScheduleItem.getEndTime());
		scheduleRepository.update(scheduleItemToBeUpdated);
		log.info("Schedule with id {} was updated correctly", id);
		return scheduleItemToBeUpdated;
	}

	@Transactional
	public void delete(int id) {
		ScheduleItem scheduleItemToBeDeleted = scheduleRepository.findById(id);
		if (scheduleItemToBeDeleted == null) {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		}
		scheduleRepository.delete(scheduleItemToBeDeleted);

	}

	public boolean checkIfSubjectIsNull(int subjectId) {
		return subjectId == 0;
	}

	public boolean checkIfStartTimeNull(LocalDateTime startTime) {
		return startTime == null;
	}

	public boolean checkIfEndTimeNull(LocalDateTime endTime) {
		return endTime == null;
	}

}
