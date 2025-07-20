package spring.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import spring.dto.GroupDto;
import spring.model.Group;

@Component
public class GroupMapper implements Mapper<GroupDto, Group> {

	@Override
	public GroupDto toDto(Group group) {
		if (group == null)
			return null;
		GroupDto groupDto = new GroupDto();
		groupDto.setId(group.getId());
		groupDto.setName(group.getName());
		groupDto.setVersion(group.getVersion());
		return groupDto;
	}

	@Override
	public Group toEntity(GroupDto groupDto) {
		if (groupDto == null)
			return null;
		Group group = new Group();
		group.setId(groupDto.getId());
		group.setName(groupDto.getName());
		group.setVersion(groupDto.getVersion());
		return group;
	}

}
