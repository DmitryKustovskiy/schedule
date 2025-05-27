package spring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.ScheduleItemDto;
import spring.mapper.ScheduleItemMapper;
import spring.model.ScheduleItem;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
import spring.repository.SubjectRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleItemService {
	private final ScheduleItemRepository scheduleRepository;
	private final GroupRepository groupRepository;
	private final SubjectRepository subjectRepository;
	private final ScheduleItemMapper scheduleItemMapper;

	public List<ScheduleItem> findByGroupName(String input) {
		return scheduleRepository.findByGroupName(input);
	}

	public List<ScheduleItemDto> findAll() {
		List<ScheduleItem> allScheduleItems = scheduleRepository.findAll();
		return scheduleItemMapper.toDtoList(allScheduleItems);
	}

	public Set<LocalDate> findAllUniqueDates() {
		List<ScheduleItem> schedules = scheduleRepository.findAll();
		return schedules.stream().map(schedule -> schedule.getStartTime().toLocalDate()).collect(Collectors.toSet());
	}

	@Transactional
	public List<ScheduleItemDto> findAllWithDetails() {
		List<ScheduleItem> schedules = scheduleRepository.findAll();
		for (ScheduleItem schedule : schedules) {
			schedule.setGroup(groupRepository.findById(schedule.getGroup().getId()).get());
			schedule.setSubject(subjectRepository.findById(schedule.getSubject().getId()).get());
		}

		return scheduleItemMapper.toDtoList(schedules);

	}

	public ScheduleItemDto findById(int id) {
		ScheduleItem existingSchedule = scheduleRepository.findById(id).orElseThrow(() -> {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		});

		return scheduleItemMapper.toDto(existingSchedule);

	}

	@Transactional
	public ScheduleItem save(ScheduleItemDto scheduleItemDto) {
		ScheduleItem scheduleItem = scheduleItemMapper.toEntity(scheduleItemDto);
		scheduleRepository.save(scheduleItem);
		log.info("Schedule {} was saved correctly", scheduleItem);

		return scheduleItem;

	}

	@Transactional
	public ScheduleItem update(ScheduleItemDto updatedScheduleItemDto, int id) {
		ScheduleItem scheduleItemToBeUpdated = scheduleRepository.findById(id).orElseThrow(() -> {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		});

		ScheduleItem updatedGroup = scheduleItemMapper.toEntity(updatedScheduleItemDto);

		if (scheduleItemToBeUpdated == null) {
			log.warn("Schedule with this id {} was not found", id);
			throw new EntityNotFoundException("Schedule not found");
		}

		scheduleItemToBeUpdated.setGroup(updatedGroup.getGroup());
		scheduleItemToBeUpdated.setSubject(updatedGroup.getSubject());
		scheduleItemToBeUpdated.setStartTime(updatedGroup.getStartTime());
		scheduleItemToBeUpdated.setEndTime(updatedGroup.getEndTime());
		scheduleRepository.save(scheduleItemToBeUpdated);
		log.info("Schedule with id {} was updated correctly", id);

		return scheduleItemToBeUpdated;
		
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
