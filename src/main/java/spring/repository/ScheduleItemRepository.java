package spring.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.model.ScheduleItem;

import java.util.List;

@Repository
public class ScheduleItemRepository {

    public List<ScheduleItem> findAll(EntityManager entityManager) {
        return entityManager.createQuery("FROM ScheduleItem", ScheduleItem.class).getResultList();
    }

    public ScheduleItem findById(EntityManager entityManager, int id) {
        return entityManager.find(ScheduleItem.class, id);
    }

    public ScheduleItem save(EntityManager entityManager, ScheduleItem scheduleItem) {
        entityManager.persist(scheduleItem);
        return scheduleItem;
    }

    public void update(EntityManager entityManager, ScheduleItem updatedItem, int id) {
        ScheduleItem scheduleItemToBeUpdated = entityManager.find(ScheduleItem.class, id);
        if (scheduleItemToBeUpdated != null && updatedItem.getGroup() != null &&
                updatedItem.getSubject() != null && updatedItem.getStartTime() != null && updatedItem.getEndTime() != null) {
            scheduleItemToBeUpdated.setGroup(updatedItem.getGroup());
            scheduleItemToBeUpdated.setSubject(updatedItem.getSubject());
            scheduleItemToBeUpdated.setStartTime(updatedItem.getStartTime());
            scheduleItemToBeUpdated.setEndTime(updatedItem.getEndTime());
        }
    }

    public void delete(EntityManager entityManager, int id) {
        ScheduleItem scheduleItemToBeRemoved = entityManager.find(ScheduleItem.class, id);
        if (scheduleItemToBeRemoved != null) {
            entityManager.remove(scheduleItemToBeRemoved);
        }
    }

    public void deleteScheduleByClassId(EntityManager entityManager, int classId) {
        ScheduleItem scheduleItemToBeRemoved = entityManager.find(ScheduleItem.class, classId);
        if (scheduleItemToBeRemoved != null) {
            entityManager.remove(scheduleItemToBeRemoved);
        }
    }

    public void deleteScheduleBySubjectId(EntityManager entityManager, int subjectId) {
        ScheduleItem scheduleItemToBeRemoved = entityManager.find(ScheduleItem.class, subjectId);
        if (scheduleItemToBeRemoved != null) {
            entityManager.remove(scheduleItemToBeRemoved);
        }
    }
}

