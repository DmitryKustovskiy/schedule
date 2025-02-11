package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.model.Group;
import spring.model.ScheduleItem;
import spring.model.Subject;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
        Set<LocalDate> uniqueDates = schedules.stream()
                .map(schedule -> schedule.getStartTime().toLocalDate())
                .collect(Collectors.toSet());
        model.addAttribute("uniqueDates", uniqueDates.stream().sorted(Comparator.naturalOrder()).toList());
        return "schedule/findAll";
    }

    @GetMapping("/{date}")
    public String findByDate(@PathVariable("date") LocalDate date, Model model) {
        List<ScheduleItem> schedules = scheduleItemService.findAllWithDetails().stream()
                .filter(schedule -> schedule.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
        model.addAttribute("schedules", schedules);
        model.addAttribute("uniqueDate", date);
        return "schedule/byDate";
    }

    @GetMapping("/new")
    public String newSchedule(@ModelAttribute("schedule") ScheduleItem scheduleItem, Model model) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "schedule/new";
    }

    @PostMapping
    public String save(@ModelAttribute("schedule") ScheduleItem scheduleItem) {
        scheduleItemService.save(scheduleItem);
        return "redirect:/schedule";
    }
}