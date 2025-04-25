package spring.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.dto.ScheduleItemDto;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;
import spring.util.DateConverter;

@Controller
@RequestMapping("/schedule")
public class ScheduleItemController {
	private final ScheduleItemService scheduleItemService;
	private final GroupService groupService;
	private final SubjectService subjectService;

	@Autowired
	public ScheduleItemController(ScheduleItemService scheduleItemService, GroupService groupService,
			SubjectService subjectService) {
		this.scheduleItemService = scheduleItemService;
		this.groupService = groupService;
		this.subjectService = subjectService;
	}

	@GetMapping
	public String findAll(Model model) {
		Set<LocalDate> uniqueDates = scheduleItemService.findAllUniqueDates();
		model.addAttribute("uniqueDates", uniqueDates.stream().sorted(Comparator.naturalOrder()));
		return "schedule/findAll";
	}

	@GetMapping("/{date}")
	public String findByDate(@PathVariable("date") LocalDate date, Model model) {
		List<ScheduleItemDto> schedules = scheduleItemService.findAllWithDetails().stream()
	            .filter(schedule -> LocalDate.parse(schedule.getStartTime().substring(0, 10)).equals(date))
	            .collect(Collectors.toList());
		model.addAttribute("schedules", schedules);
		model.addAttribute("uniqueDate", date);
		System.out.println("Schedules size: " + schedules.size());
		return "schedule/byDate";
	}

	@GetMapping("/new")
	public String newSchedule(@ModelAttribute("scheduleDto") ScheduleItemDto scheduleItemDto, Model model) {
		model.addAttribute("groups", groupService.findAll());
		model.addAttribute("subjects", subjectService.findAll());
		return "schedule/new";
	}

	@PostMapping("/new")
	public String save(@ModelAttribute("scheduleDto") ScheduleItemDto scheduleItemDto, Model model) {
		Map<String, String> errors = new HashMap<>();

		if (scheduleItemService.checkIfSubjectIsNull(scheduleItemDto.getSubjectDto().getId())) {
			errors.put("errorMessageSubject", "Sorry, you should enter Subject");
		}
		if (scheduleItemService.checkIfStartTimeNull(DateConverter.stringToDate(scheduleItemDto.getStartTime()))) {
			errors.put("errorMessageStartTime", "Sorry, you should enter Start time");
		}
		if (scheduleItemService.checkIfEndTimeNull(DateConverter.stringToDate(scheduleItemDto.getEndTime()))) {
			errors.put("errorMessageEndTime", "Sorry, you should enter End time");
		}

		if (!errors.isEmpty()) {
			model.addAllAttributes(errors);
			model.addAttribute("groups", groupService.findAll());
			model.addAttribute("subjects", subjectService.findAll());
			return "schedule/new";
		}
		int groupId = scheduleItemDto.getGroupDto().getId();
	    int subjectId = scheduleItemDto.getSubjectDto().getId();

	    scheduleItemDto.setGroupDto(groupService.findById(groupId));
	    scheduleItemDto.setSubjectDto(subjectService.findById(subjectId));
		scheduleItemService.save(scheduleItemDto);
		return "redirect:/schedule";
	}

}