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
import java.util.*;
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

    @PostMapping("/new")
    public String save(@ModelAttribute("schedule") ScheduleItem scheduleItem, Model model) {
        Map<String, String> errors = new HashMap<>();

        if (scheduleItemService.checkIfSubjectIsNull(scheduleItem.getSubject().getId())) {
            errors.put("errorMessageSubject", "Sorry, you should enter Subject");
        }
        if (scheduleItemService.checkIfStartTimeNull(scheduleItem.getStartTime())) {
            errors.put("errorMessageStartTime", "Sorry, you should enter Start time");
        }
        if (scheduleItemService.checkIfEndTimeNull(scheduleItem.getEndTime())) {
            errors.put("errorMessageEndTime", "Sorry, you should enter End time");
        }

        if (!errors.isEmpty()) {
            model.addAllAttributes(errors);
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("subjects", subjectService.findAll());
            return "schedule/new";
        }

        scheduleItemService.save(scheduleItem);
        return "redirect:/schedule";
    }
}