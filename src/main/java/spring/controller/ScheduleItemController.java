package spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.dto.ScheduleItemDto;
import spring.model.ScheduleItem;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleItemController {
	
	private final ScheduleItemService scheduleItemService;
	private final GroupService groupService;
	private final SubjectService subjectService;

	@GetMapping("/search")
	public String searchSchedules(@RequestParam(value = "query", required = false) String query, Model model) {
		List<ScheduleItem> schedules = new ArrayList<>();

		if (query != null && !query.isBlank()) {
			schedules = scheduleItemService.findByGroupName(query);
		}
		model.addAttribute("schedules", schedules);
		return "schedule/search";
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
		return "schedule/byDate";
	}

	@GetMapping("/new")
	public String newSchedule(@ModelAttribute("scheduleDto") ScheduleItemDto scheduleItemDto, Model model) {
		model.addAttribute("groups", groupService.findAll());
		model.addAttribute("subjects", subjectService.findAll());
		return "schedule/new";
	}

	@PostMapping("/new")
	public String save(@ModelAttribute("scheduleDto") @Valid ScheduleItemDto scheduleItemDto,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("groups", groupService.findAll());
			model.addAttribute("subjects", subjectService.findAll());
			return "schedule/new";
		}
		scheduleItemService.save(scheduleItemDto);
		return "redirect:/schedule";
	}

}