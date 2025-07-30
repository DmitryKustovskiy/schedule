package spring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.ScheduleItemDto;
import spring.mapper.ScheduleItemMapper;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.model.Subject;
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
	
	public ScheduleItem findByDate(LocalDateTime date) {
		 return scheduleRepository.findByDate(date).orElseThrow(()
				 -> new EntityNotFoundException("Schedule was not found"));
		 
	}

	public Set<LocalDate> findAllUniqueDates() {
		List<ScheduleItem> schedules = scheduleRepository.findAll();
		return schedules.stream().map(schedule -> schedule.getStartTime().toLocalDate()).collect(Collectors.toSet());
	}

	@Transactional
	public List<ScheduleItemDto> findAllWithDetails() {
		List<ScheduleItem> schedules = scheduleRepository.findAllWithDetails();
		return scheduleItemMapper.toDtoList(schedules);
	}

	public ScheduleItemDto findById(int id) {
		ScheduleItem scheduleItem = scheduleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Schedule was not found"));

		return scheduleItemMapper.toDto(scheduleItem);

	}

	@Transactional
	public ScheduleItem save(ScheduleItemDto scheduleItemDto) {
		Group group = groupRepository.findById(scheduleItemDto.getGroupDto().getId())
				.orElseThrow(() -> new EntityNotFoundException("Group was not found"));

		Subject subject = subjectRepository.findById(scheduleItemDto.getSubjectDto().getId())
				.orElseThrow(() -> new EntityNotFoundException("Subject was not found"));

		ScheduleItem scheduleItem = scheduleItemMapper.toEntity(scheduleItemDto);
		scheduleItem.setGroup(group);
		scheduleItem.setSubject(subject);
		scheduleRepository.save(scheduleItem);
		log.info("Schedule {} was saved correctly", scheduleItem);
		return scheduleItem;
	}

	@Transactional
	public ScheduleItem update(ScheduleItemDto dto, int id) {
		ScheduleItem schedule = scheduleRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Schedule was not found"));
		
		Group group = groupRepository.findById(dto.getGroupDto().getId())
		        .orElseThrow(() -> new EntityNotFoundException("Group was not found"));
		    Subject subject = subjectRepository.findById(dto.getSubjectDto().getId())
		        .orElseThrow(() -> new EntityNotFoundException("Subject was not found"));
		    
		ScheduleItem updatedSchedule = scheduleItemMapper.toEntity(dto);
		
		schedule.setGroup(group);
		schedule.setSubject(subject);
		schedule.setStartTime(updatedSchedule.getStartTime());
		schedule.setEndTime(updatedSchedule.getEndTime());
		schedule.setVersion(updatedSchedule.getVersion());
	    
		scheduleRepository.save(schedule);
		log.info("Schedule with id {} was updated correctly", dto.getId());
		return schedule;
	}

	@Transactional
	public void delete(int id) {
		ScheduleItem scheduleItem = scheduleRepository.findById(id).orElseThrow(() -> {
			throw new EntityNotFoundException("Schedule was not found");
		});

		scheduleRepository.deleteById(scheduleItem.getId());
		log.info("Schedule with id {} was deleted correctly", id);

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
