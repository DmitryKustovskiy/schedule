package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.model.ScheduleItem;
import spring.repository.GroupRepository;
import spring.repository.ScheduleItemRepository;
import spring.repository.SubjectRepository;

import java.util.List;

@Service
public class ScheduleItemService {
    private final ScheduleItemRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ScheduleItemService(ScheduleItemRepository scheduleRepository, GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.scheduleRepository=scheduleRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<ScheduleItem> findAll() {
        return scheduleRepository.findAll();
    }

    public List<ScheduleItem> findAllWithDetails() {
        List<ScheduleItem> schedules = scheduleRepository.findAll();
        for (ScheduleItem schedule : schedules) {
            schedule.setGroup(groupRepository.findById(schedule.getClassId()));
            schedule.setSubject(subjectRepository.findById(schedule.getSubjectId()));
        }
        return schedules;
    }

    public ScheduleItem findById(int id) {
        return scheduleRepository.findById(id);
    }

    public ScheduleItem save(ScheduleItem scheduleItem) {
        return scheduleRepository.save(scheduleItem);
    }

    public void update(ScheduleItem scheduleItem, int id) {
        scheduleRepository.update(scheduleItem, id);
    }

    public boolean delete(int id) {
        return scheduleRepository.delete(id);
    }

}
