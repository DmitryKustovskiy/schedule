package spring.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spring.dto.ScheduleItemDto;
import spring.model.ScheduleItem;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleItemMapper {

	public static ScheduleItemDto toDto(ScheduleItem scheduleItem) {
		if (scheduleItem == null)
			return null;
		ScheduleItemDto scheduleItemDto = new ScheduleItemDto();
		scheduleItemDto.setId(scheduleItem.getId());
		scheduleItemDto.setStartTime(scheduleItem.getStartTime());
		scheduleItemDto.setEndTime(scheduleItem.getEndTime());
		scheduleItemDto.setGroup(scheduleItem.getGroup());
		scheduleItemDto.setSubject(scheduleItem.getSubject());
		return scheduleItemDto;

	}

	public static ScheduleItem toEntity(ScheduleItemDto scheduleItemDto) {
		if (scheduleItemDto == null)
			return null;
		ScheduleItem scheduleItem = new ScheduleItem();
		scheduleItem.setId(scheduleItemDto.getId());
		scheduleItem.setStartTime(scheduleItemDto.getStartTime());
		scheduleItem.setEndTime(scheduleItemDto.getEndTime());
		scheduleItem.setGroup(scheduleItemDto.getGroup());
		scheduleItem.setSubject(scheduleItemDto.getSubject());
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
