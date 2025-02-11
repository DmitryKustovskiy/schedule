package spring.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import spring.model.Group;

@Repository
public class GroupRepository {
    private final JdbcTemplate template;

    private static final String SAVE_SQL = "INSERT INTO class (name) values(?) RETURNING id";
    private static final String FIND_ALL_SQL = "SELECT * FROM class";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM class WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE class set name=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM class WHERE id=?";

    @Autowired
    public GroupRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Group> findAll() {
        return template.query(FIND_ALL_SQL, new BeanPropertyRowMapper<>(Group.class));
    }

    public Group findById(int id) {
        return template.query(FIND_BY_ID_SQL, new BeanPropertyRowMapper<>(Group.class), id)
                .stream().findFirst()
                .orElse(null);
    }

    public Group save(Group group) {
        group.setId(template.queryForObject(SAVE_SQL, Integer.class, group.getName()));
        return group;
    }

    public void update(Group group, int id) {
        template.update(UPDATE_SQL, group.getName(), id);
    }

    public boolean delete(int id) {
        return template.update(DELETE_SQL, id) > 0;
    }
}
