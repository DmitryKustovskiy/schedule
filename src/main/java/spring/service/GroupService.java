package spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findById(int id) {
        return groupRepository.findById(id);
    }

    public Group findGroupByStudentId(int id) {
        Student student = studentRepository.findById(id);
        return groupRepository.findById(student.getClassId());
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    public void update(Group group, int id) {
        groupRepository.update(group, id);
    }

    public boolean delete(int id) {
        return groupRepository.delete(id);
    }

    public boolean checkIfGroupExists(String groupName) {
        List<Group> allGroups = groupRepository.findAll();
        return (allGroups.stream().anyMatch(
                existingGroup -> existingGroup.getName().equalsIgnoreCase(groupName)));
    }

    public boolean checkIfGroupIsNotEmpty(int id) {
        return studentRepository.findAll().stream().anyMatch(student -> student.getClassId() == id);
    }

}
