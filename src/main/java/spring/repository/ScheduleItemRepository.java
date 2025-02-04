package spring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.model.Group;
import spring.model.ScheduleItem;

import java.util.List;

@Repository
public class ScheduleItemRepository {
    private static final String SAVE_SQL = "INSERT INTO schedule (class_id, subject_id, start_time, end_time) values(?,?,?,?) RETURNING id";
    private static final String FIND_ALL_SQL = "SELECT * FROM schedule";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM schedule WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE schedule set class_id=?, subject_id=?, start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM schedule WHERE id=?";

    private final JdbcTemplate template;

    @Autowired
    public ScheduleItemRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<ScheduleItem> findAll() {
        return template.query(FIND_ALL_SQL, new BeanPropertyRowMapper<>(ScheduleItem.class));
    }

    public ScheduleItem findById(int id) {
        return template.query(FIND_BY_ID_SQL, new BeanPropertyRowMapper<>(ScheduleItem.class), id)
                .stream().findFirst()
                .orElse(null);
    }

    public ScheduleItem save(ScheduleItem scheduleItem) {
        scheduleItem.setId(template.queryForObject(SAVE_SQL,
                Integer.class, scheduleItem.getGroup().getId(),
                scheduleItem.getSubject().getId(), scheduleItem.getStartTime(), scheduleItem.getEndTime()));
        return scheduleItem;
    }

    public void update(ScheduleItem scheduleItem, int id) {
        template.update(UPDATE_SQL, scheduleItem.getGroup().getId(),
                scheduleItem.getSubject().getId(), scheduleItem.getStartTime(), scheduleItem.getEndTime(), id);
    }

    public boolean delete(int id) {
        return template.update(DELETE_SQL, id) > 0;
    }
}

