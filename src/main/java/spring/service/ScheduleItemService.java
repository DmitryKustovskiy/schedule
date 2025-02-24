package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.model.ScheduleItem;
import spring.repository.ScheduleItemRepository;
import spring.repository.SubjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleItemService {
    private final ScheduleItemRepository scheduleRepository;
    private final GroupService groupService;
    private final SubjectService subjectService;

    @Autowired
    public ScheduleItemService(ScheduleItemRepository scheduleRepository, GroupService groupService, SubjectService subjectService) {
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
        this.subjectService = subjectService;
    }

    public List<ScheduleItem> findAll() {
        return scheduleRepository.findAll();
    }

    public List<ScheduleItem> findAllWithDetails() {
        List<ScheduleItem> schedules = scheduleRepository.findAll();
        for (ScheduleItem schedule : schedules) {
            schedule.setGroup(groupService.findById(schedule.getGroup().getId()));
            schedule.setSubject(subjectService.findById(schedule.getSubject().getId()));
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

    public void delete(int id) {
        scheduleRepository.delete(id);
    }

    public void deleteByClassId(int classId) {
        scheduleRepository.deleteScheduleByClassId(classId);
    }

    public void deleteBySubjectId(int subjectId) {
        scheduleRepository.deleteScheduleBySubjectId(subjectId);
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
