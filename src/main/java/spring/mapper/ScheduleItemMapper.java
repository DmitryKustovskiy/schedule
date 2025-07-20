package spring.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.dto.ScheduleItemDto;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.util.DateConverter;

@Component
@RequiredArgsConstructor
public class ScheduleItemMapper implements Mapper<ScheduleItemDto, ScheduleItem> {
	private final GroupMapper groupMapper;
	private final SubjectMapper subjectMapper;

	@Override
	public ScheduleItemDto toDto(ScheduleItem scheduleItem) {
		if (scheduleItem == null)
			return null;
		ScheduleItemDto scheduleItemDto = new ScheduleItemDto();
		scheduleItemDto.setId(scheduleItem.getId());
		scheduleItemDto.setStartTime(DateConverter.dateToString(scheduleItem.getStartTime()));
		scheduleItemDto.setEndTime(DateConverter.dateToString(scheduleItem.getEndTime()));
		scheduleItemDto.setGroupDto(groupMapper.toDto(scheduleItem.getGroup()));
		scheduleItemDto.setSubjectDto(subjectMapper.toDto(scheduleItem.getSubject()));
		scheduleItemDto.setVersion(scheduleItem.getVersion());
		return scheduleItemDto;
	}

	@Override
	public ScheduleItem toEntity(ScheduleItemDto scheduleItemDto) {
		if (scheduleItemDto == null)
			return null;
		ScheduleItem scheduleItem = new ScheduleItem();
		scheduleItem.setId(scheduleItemDto.getId());
		scheduleItem.setStartTime(DateConverter.stringToDate(scheduleItemDto.getStartTime()));
		scheduleItem.setEndTime(DateConverter.stringToDate(scheduleItemDto.getEndTime()));
		scheduleItem.setGroup(groupMapper.toEntity(scheduleItemDto.getGroupDto()));
		scheduleItem.setSubject(subjectMapper.toEntity(scheduleItemDto.getSubjectDto()));
		scheduleItem.setVersion(scheduleItemDto.getVersion());
		return scheduleItem;
	}

}
