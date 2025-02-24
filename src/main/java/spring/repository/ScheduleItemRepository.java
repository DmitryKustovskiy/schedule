package spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.model.ScheduleItem;
import spring.service.GroupService;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ScheduleItemRepository {
    @PersistenceContext
    private EntityManager manager;

    public List<ScheduleItem> findAll() {
        return manager.createQuery("FROM ScheduleItem", ScheduleItem.class).getResultList();
    }

    public ScheduleItem findById(int id) {
        return manager.find(ScheduleItem.class, id);
    }

    @Transactional
    public ScheduleItem save(ScheduleItem scheduleItem) {
        manager.persist(scheduleItem);
        return scheduleItem;
    }

    @Transactional
    public void update(ScheduleItem scheduleItem, int id) {
        ScheduleItem updatedScheduleItem = manager.find(ScheduleItem.class, id);
        updatedScheduleItem.setGroup(scheduleItem.getGroup());
        updatedScheduleItem.setSubject(scheduleItem.getSubject());
        updatedScheduleItem.setStartTime(scheduleItem.getStartTime());
        updatedScheduleItem.setEndTime(scheduleItem.getEndTime());
    }

    @Transactional
    public void delete(int id) {
        ScheduleItem scheduleItemToBeRemoved = manager.find(ScheduleItem.class, id);
        manager.remove(scheduleItemToBeRemoved);
    }

    @Transactional
    public void deleteScheduleByClassId(int classId) {
        ScheduleItem scheduleItemToBeRemoved = manager.find(ScheduleItem.class, classId);
        if (scheduleItemToBeRemoved != null) {
            manager.remove(scheduleItemToBeRemoved);
        }
    }

    @Transactional
    public void deleteScheduleBySubjectId(int subjectId) {
        ScheduleItem scheduleItemToBeRemoved = manager.find(ScheduleItem.class, subjectId);
        if (scheduleItemToBeRemoved != null) {
            manager.remove(scheduleItemToBeRemoved);
        }
    }
}

