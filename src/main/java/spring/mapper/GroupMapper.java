package spring.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spring.dto.GroupDto;
import spring.dto.StudentDto;
import spring.model.Group;
import spring.model.Student;

public class GroupMapper {

	public static GroupDto toDto(Group group) {
		if (group == null)
			return null;
		GroupDto groupDto = new GroupDto();
		groupDto.setId(group.getId());
		groupDto.setName(group.getName());
		return groupDto;
	}

	public static Group toEntity(GroupDto groupDto) {
		if (groupDto == null)
			return null;
		Group group = new Group();
		group.setId(groupDto.getId());
		group.setName(groupDto.getName());
		return group;

	}

	public static List<GroupDto> toDtoList(List<Group> groups) {
		if (groups == null)
			return null;
		return groups.stream().map(GroupMapper::toDto).toList();
	}

	public static List<Group> toEntityList(List<GroupDto> groupDtos) {
		if (groupDtos == null)
			return null;
		return groupDtos.stream().map(GroupMapper::toEntity).toList();
	}

}
