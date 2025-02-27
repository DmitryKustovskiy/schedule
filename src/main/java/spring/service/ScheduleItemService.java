package spring.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.configuration.EntityManagerUtil;
import spring.model.ScheduleItem;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
import spring.repository.SubjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleItemService {
    private final ScheduleItemRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ScheduleItemService(ScheduleItemRepository scheduleRepository, GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<ScheduleItem> findAll() {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return scheduleRepository.findAll(entityManager);
        }
    }

    public List<ScheduleItem> findAllWithDetails() {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            List<ScheduleItem> schedules = scheduleRepository.findAll(entityManager);
            for (ScheduleItem schedule : schedules) {
                schedule.setGroup(groupRepository.findById(entityManager, schedule.getGroup().getId()));
                schedule.setSubject(subjectRepository.findById(entityManager, schedule.getSubject().getId()));
            }
            return schedules;
        }
    }

    public ScheduleItem findById(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return entityManager.find(ScheduleItem.class, id);
        }
    }

    public ScheduleItem save(ScheduleItem scheduleItem) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                scheduleRepository.save(entityManager, scheduleItem);
                transaction.commit();
                return scheduleItem;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void update(ScheduleItem scheduleItem, int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                scheduleRepository.update(entityManager, scheduleItem, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public void delete(int id) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                scheduleRepository.delete(entityManager, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public void deleteByClassId(int classId) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                scheduleRepository.deleteScheduleByClassId(entityManager, classId);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public void deleteBySubjectId(int subjectId) {
        try (EntityManager entityManager = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                scheduleRepository.deleteScheduleBySubjectId(entityManager, subjectId);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }

    public boolean checkIfSubjectIsNull(int subjectId) {
        return subjectId == 0;
    }

    public boolean checkIfStartTimeNull(LocalDateTime startTime) {
        return startTime == null;
    }

    public boolean checkIfEndTimeNull(LocalDateTime endTime) {
        return endTime == null;
    }

}
