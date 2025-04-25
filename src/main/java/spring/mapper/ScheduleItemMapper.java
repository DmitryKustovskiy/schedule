package spring.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import spring.dto.ScheduleItemDto;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.util.DateConverter;

public class ScheduleItemMapper {

	public static ScheduleItemDto toDto(ScheduleItem scheduleItem) {
		if (scheduleItem == null)
			return null;
		ScheduleItemDto scheduleItemDto = new ScheduleItemDto();
		scheduleItemDto.setId(scheduleItem.getId());
		scheduleItemDto.setStartTime(DateConverter.dateToString(scheduleItem.getStartTime()));
		scheduleItemDto.setEndTime(DateConverter.dateToString(scheduleItem.getEndTime()));
		scheduleItemDto.setGroupDto(GroupMapper.toDto(scheduleItem.getGroup()));
		scheduleItemDto.setSubjectDto(SubjectMapper.toDto(scheduleItem.getSubject()));
		return scheduleItemDto;

	}

	public static ScheduleItem toEntity(ScheduleItemDto scheduleItemDto) {
		if (scheduleItemDto == null)
			return null;
		ScheduleItem scheduleItem = new ScheduleItem();
		scheduleItem.setId(scheduleItemDto.getId());
		scheduleItem.setStartTime(DateConverter.stringToDate(scheduleItemDto.getStartTime()));
		scheduleItem.setEndTime(DateConverter.stringToDate(scheduleItemDto.getEndTime()));
		scheduleItem.setGroup(GroupMapper.toEntity(scheduleItemDto.getGroupDto()));
		scheduleItem.setSubject(SubjectMapper.toEntity(scheduleItemDto.getSubjectDto()));
		return scheduleItem;

	}

	public static List<ScheduleItemDto> toDtoList(List<ScheduleItem> scheduleItems) {
		if (scheduleItems == null)
			return null;
		return scheduleItems.stream().map(ScheduleItemMapper::toDto).toList();

	}

	public static List<ScheduleItem> toEntityList(List<ScheduleItemDto> scheduleItemDtos) {
		if (scheduleItemDtos == null)
			return null;
		return scheduleItemDtos.stream().map(ScheduleItemMapper::toEntity).toList();

	}

}
