package spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.model.Group;
import spring.model.Student;
import spring.repository.GroupRepository;
import spring.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(int id) {
        return studentRepository.findById(id);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void update(Student student, int id) {
        studentRepository.update(student, id);
    }

    public boolean setGroup(Student student, int id) {
        return studentRepository.setGroup(student, id);
    }

    public boolean delete(int id) {
        return studentRepository.delete(id);
    }

    public boolean checkIfClassIdExists(int classId) {
        return groupRepository.findAll().stream().anyMatch(existingClassId -> existingClassId.getId() == classId);
    }

}
