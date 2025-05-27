package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.GroupDto;
import spring.mapper.GroupMapper;
import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {
	
	private final GroupRepository groupRepository;
	private final StudentRepository studentRepository;

	public List<GroupDto> findAll() {
		List<Group> allGroups = groupRepository.findAll();
		return GroupMapper.toDtoList(allGroups);
	}

	public GroupDto findById(int id) {
		Group group = groupRepository.findById(id).orElseThrow(() -> {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Not found");
		});
		
		return GroupMapper.toDto(group);

	}

	public GroupDto findGroupByStudentId(int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Not found");
		});
		Group group = groupRepository.findById(student.getGroup().getId()).orElseThrow(() -> {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Not found");
		});

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
		Group group = groupRepository.findById(id).orElseThrow(() -> {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group was not found");
		});

		group.setName(updatedGroupDto.getName());
		Group updatedGroup = groupRepository.save(group);
		log.info("Group with id {} was updated correctly", id);
		return updatedGroup;

	}

	@Transactional
	public void delete(int id) {
		Group group = groupRepository.findById(id).orElseThrow(() -> {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group not found");
		});

		groupRepository.deleteById(group.getId());
		log.info("Group with id {} was deleted correctly", id);

	}

	public boolean checkIfGroupExists(String groupName) {
		List<Group> allGroups = groupRepository.findAll();
		return allGroups.stream().anyMatch(existingGroup -> existingGroup.getName().equalsIgnoreCase(groupName));

	}

	public boolean checkIfGroupIsNotEmpty(int id) {
		return studentRepository.findAll().stream().anyMatch(student -> student.getGroup().getId() == id);

	}
}
