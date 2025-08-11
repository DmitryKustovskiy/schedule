package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.dto.GroupDto;
import spring.exception.EntityAlreadyExistsException;
import spring.exception.GroupNotEmptyException;
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
	private final GroupMapper groupMapper;

	public List<GroupDto> findAll() {
		List<Group> allGroups = groupRepository.findAll();
		return groupMapper.toDtoList(allGroups);

	}

	public GroupDto findById(int id) {
		Group group = findGroup(id);
		return groupMapper.toDto(group);

	}

	public GroupDto findGroupByStudentGroupId(int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> {
			log.warn("Student with id {} was not found", id);
			throw new EntityNotFoundException("Student was not found");
		});
		Group group = groupRepository.findById(student.getGroup().getId()).orElseThrow(() -> {
			log.warn("Group with id {} was not found", student.getGroup().getId());
			throw new EntityNotFoundException("Group was not found");
		});
		return groupMapper.toDto(group);

	}

	@Transactional
	public Group save(GroupDto groupDto) {
		if (groupRepository.existsByNameIgnoreCase(groupDto.getName())) {
			throw new EntityAlreadyExistsException("Group with this name already exists");
		}

		Group group = groupMapper.toEntity(groupDto);
		Group savedGroup = groupRepository.save(group);
		log.info("Group {} was saved correctly", savedGroup);
		return savedGroup;

	}

	@Transactional
	public Group update(GroupDto updatedGroupDto, int id) {
		if (groupRepository.existsByNameIgnoreCase(updatedGroupDto.getName())) {
			throw new EntityAlreadyExistsException("Group with this name already exists");
		}

		Group group = findGroup(id);

		if (!group.getVersion().equals(updatedGroupDto.getVersion())) {
			throw new OptimisticLockException();
		}
		group.setName(updatedGroupDto.getName());

		log.info("Group with id {} was updated correctly", id);
		return groupRepository.save(group);

	}

	@Transactional
	public void delete(int id) {
		Group group = findGroup(id);

		if (studentRepository.existsByGroupId(group.getId())) {
			throw new GroupNotEmptyException("You can't delete group that is not empty");
		}

		groupRepository.delete(group);
		log.info("Group with id {} was deleted correctly", id);

	}

	private Group findGroup(int id) {
		return groupRepository.findById(id).orElseThrow(() -> {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Sorry, group was not found!");
		});

	}

}
