package spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
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

	public List<Group> findAll() {
		return groupRepository.findAll();
	}

	public Group findById(int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with id {} was not found", id);
			throw new EntityNotFoundException("Not found");
		}
		return existingGroup;

	}

	public Group findGroupByStudentId(int id) {
		Student student = studentRepository.findById(id);
		return groupRepository.findById(student.getGroup().getId());
	}

	@Transactional
	public Group save(Group group) {
		groupRepository.save(group);
		log.info("Group {} was saved correctly", group);
		return group;
	}

	@Transactional
	public Group update(Group updatedGroup, int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group was not found");
		}
		existingGroup.setName(updatedGroup.getName());
		log.info("Group with id {} was updated correctly", id);
		return groupRepository.update(existingGroup);

	}

	@Transactional
	public void delete(int id) {
		Group existingGroup = groupRepository.findById(id);
		if (existingGroup == null) {
			log.warn("Group with this id {} was not found", id);
			throw new EntityNotFoundException("Group not found");
		}
		log.info("Group with id {} was deleted correctly", id);
		groupRepository.delete(existingGroup);
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
