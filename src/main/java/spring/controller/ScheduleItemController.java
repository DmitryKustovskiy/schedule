package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.model.ScheduleItem;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/schedule")
public class ScheduleItemController {

    private final ScheduleItemService scheduleItemService;
    private final GroupService groupService;
    private final SubjectService subjectService;

    @Autowired
    public ScheduleItemController(ScheduleItemService scheduleItemService, GroupService groupService, SubjectService subjectService) {
        this.scheduleItemService = scheduleItemService;
        this.groupService = groupService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<ScheduleItem> schedules = scheduleItemService.findAll();

        // Собираем уникальные даты
        Set<LocalDate> uniqueDates = schedules.stream()
                .map(schedule -> schedule.getStartTime().toLocalDate()) // Берем только дату
                .collect(Collectors.toSet()); // Убираем дубли

        model.addAttribute("uniqueDates", uniqueDates);
        return "schedule/findAll";
    }

    @GetMapping("/{date}")
    public String findByDate(@PathVariable("date") LocalDate date, Model model) {
        List<ScheduleItem> schedules = scheduleItemService.findAllWithDetails().stream()
                .filter(schedule -> schedule.getStartTime().toLocalDate().equals(date)) // Фильтруем по дате
                .collect(Collectors.toList());
        model.addAttribute("schedules", schedules);
        model.addAttribute("date", date);
        return "schedule/byDate";
    }
}