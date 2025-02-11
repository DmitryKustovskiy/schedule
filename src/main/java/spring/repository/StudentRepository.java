package spring.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import spring.model.Student;

@Repository
public class StudentRepository {
    private final JdbcTemplate template;

    private static final String SAVE_SQL = "INSERT INTO student (first_name, last_name, class_id) values(?,?,?) RETURNING id";
    private static final String FIND_ALL_SQL = "SELECT * FROM student";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM student WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE student SET first_name=?, last_name=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM student WHERE id=?";
    private static final String UPDATE_GROUP_FROM_STUDENT = "UPDATE student SET class_id = ? WHERE id = ?";

    @Autowired
    public StudentRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Student> findAll() {
        return template.query(FIND_ALL_SQL, new BeanPropertyRowMapper<>(Student.class));
    }

    public Student findById(int id) {
        return template.query(FIND_BY_ID_SQL, new BeanPropertyRowMapper<>(Student.class), id).stream().findFirst()
                .orElse(null);
    }

    public Student save(Student student) {
        student.setId(template.queryForObject(SAVE_SQL, Integer.class,
                student.getFirstName(),
                student.getLastName(),
                student.getClassId()));
        return student;
    }

    public void update(Student student, int id) {
        template.update(UPDATE_SQL, student.getFirstName(), student.getLastName(), id);
    }

    public boolean setGroup(Student student, int id) {
        return template.update(UPDATE_GROUP_FROM_STUDENT, student.getClassId(), id) > 0;
    }

    public boolean delete(int id) {
        return template.update(DELETE_SQL, id) > 0;
    }

}
