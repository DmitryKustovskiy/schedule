package spring.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import spring.model.Group;
import spring.model.Subject;

@Repository
public class SubjectRepository {
    private final JdbcTemplate template;

    private static final String SAVE_SQL = "INSERT INTO subject (name) values(?) RETURNING id";
    private static final String FIND_ALL_SQL = "SELECT * FROM subject";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM subject WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE subject set name=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM subject WHERE id=?";

    @Autowired
    public SubjectRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Subject> findAll() {
        return template.query(FIND_ALL_SQL, new BeanPropertyRowMapper<>(Subject.class));
    }

    public Subject findById(int id) {
        return template.query(FIND_BY_ID_SQL, new BeanPropertyRowMapper<>(Subject.class), id)
                .stream().findFirst()
                .orElse(null);
    }

    public Subject save(Subject subject) {
        subject.setId(template.queryForObject(SAVE_SQL, Integer.class, subject.getName()));
        return subject;
    }

    public void update(Subject subject, int id) {
        template.update(UPDATE_SQL, subject.getName(), id);
    }

    public boolean delete(int id) {
        return template.update(DELETE_SQL, id) > 0;
    }

}
