package spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
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
import spring.dto.GroupDto;
import spring.dto.ScheduleItemDto;
import spring.dto.SubjectDto;
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
		List<ScheduleItemDto> scheduleDtos = new ArrayList<>();

		if (query != null && !query.isBlank()) {
			scheduleDtos = scheduleItemService.findByGroupName(query);
		}
		model.addAttribute("scheduleDtos", scheduleDtos);

		return "schedule/search";
		
	}

	@GetMapping
	public String findAll(Model model) {
		Set<LocalDate> uniqueDates = scheduleItemService.findAllUniqueDates();
		model.addAttribute("uniqueDates", uniqueDates.stream().sorted(Comparator.naturalOrder()));
		model.addAttribute("schedules", scheduleItemService.findAll());
	    model.addAttribute("scheduleDto", new ScheduleItemDto());
		model.addAttribute("allGroups", groupService.findAll());
	    model.addAttribute("allSubjects", subjectService.findAll());

		return "schedule/findAll";
		
	}

	@GetMapping("/{date}")
	public String findByDate(@PathVariable("date") LocalDate date, Model model) {
		List<ScheduleItemDto> schedules = scheduleItemService.findAllWithDetails().stream()
				.filter(schedule -> LocalDate.parse(schedule.getStartTime().substring(0, 10)).equals(date))
				.collect(Collectors.toList());
		model.addAttribute("schedules", schedules);
		model.addAttribute("uniqueDate", date);
		model.addAttribute("allGroups", groupService.findAll());
	    model.addAttribute("allSubjects", subjectService.findAll());

		return "schedule/byDate";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") int id, Model model) {
		ScheduleItemDto scheduleItemDto = scheduleItemService.findById(id);
		model.addAttribute("schedule", scheduleItemDto);
		model.addAttribute("groups", groupService.findAll());
		model.addAttribute("subjects", subjectService.findAll());

		return "schedule/edit";
	}

	@PostMapping("/{id}/edit")
	public String update(@ModelAttribute("schedule") @Valid ScheduleItemDto dto, BindingResult bindingResult,
			@PathVariable("id") int id, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("groups", groupService.findAll());
			model.addAttribute("subjects", subjectService.findAll());
			return "schedule/edit";
		}
		scheduleItemService.update(dto, id);
		return "redirect:/schedule";
	}
	
	@PostMapping("/{date}/add")
	public String addSchedule(
	        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	        @RequestParam("groupId") int groupId,
	        @RequestParam("subjectId") int subjectId,
	        @RequestParam("startTime") String startTime,
	        @RequestParam("endTime") String endTime
	) {
	    String startDateTime = date + "T" + startTime;
	    String endDateTime = date + "T" + endTime;

	    GroupDto group = groupService.findById(groupId);
	    SubjectDto subject = subjectService.findById(subjectId);

	    ScheduleItemDto newItem = new ScheduleItemDto();
	    newItem.setGroupDto(group);
	    newItem.setSubjectDto(subject);
	    newItem.setStartTime(startDateTime);
	    newItem.setEndTime(endDateTime);

	    scheduleItemService.save(newItem);

	    return "redirect:/schedule/" + date;
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

	@PostMapping("/{id}")
	public String delete(@PathVariable("id") int id, Model model) {
		scheduleItemService.delete(id);

		return "redirect:/schedule";
	}

}