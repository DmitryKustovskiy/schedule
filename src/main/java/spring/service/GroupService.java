package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import spring.dto.GroupDto;
import spring.mapper.GroupMapper;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
@Transactional(readOnly = true)
public class GroupService {
	private final GroupRepository groupRepository;
	private final StudentRepository studentRepository;
	private static final Logger log = LoggerFactory.getLogger(GroupService.class);

	@Autowired
	public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
		this.groupRepository = groupRepository;
		this.studentRepository = studentRepository;
	}

	public List<GroupDto> findAll() {
		List<Group> allGroups = groupRepository.findAll();
		return GroupMapper.toDtoList(allGroups);
	}

	public GroupDto findById(int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Not found");
		}
		return GroupMapper.toDto(existingGroup);

	}

	public GroupDto findGroupByStudentId(int id) {
		Student student = studentRepository.findById(id);
		Group group = groupRepository.findById(student.getGroup().getId());
		return GroupMapper.toDto(group);
	}

	@Transactional
	public Group save(GroupDto groupDto) {
		Group group = GroupMapper.toEntity(groupDto);
		groupRepository.save(group);
		log.info("Group {} was saved correctly", group);
		return group;
	}

	@Transactional
	public Group update(GroupDto updatedGroupDto, int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group was not found");
		}
		existingGroup.setName(updatedGroupDto.getName());
		Group updatedGroup = groupRepository.update(existingGroup);
		log.info("Group with id {} was updated correctly", id);
		return updatedGroup;

	}

	@Transactional
	public void delete(int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group not found");
		}
		groupRepository.delete(existingGroup);
		log.info("Group with id {} was deleted correctly", id);
	}

	public boolean checkIfNull(String groupName) {
		return groupName.isEmpty();
	}

	public boolean checkIfGroupExists(String groupName) {
		List<Group> allGroups = groupRepository.findAll();
		return allGroups.stream().anyMatch(existingGroup -> existingGroup.getName().equalsIgnoreCase(groupName));

	}

	public boolean checkIfGroupIsNotEmpty(int id) {
		return studentRepository.findAll().stream().anyMatch(student -> student.getGroup().getId() == id);

	}
}
