package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import spring.dto.GroupDto;
import spring.model.Group;
import spring.service.GroupService;
import spring.service.ScheduleItemService;

@Controller
@RequestMapping("/groups")
public class GroupController {
	private final GroupService groupService;

	@Autowired
	public GroupController(GroupService groupService, ScheduleItemService scheduleItemService) {
		this.groupService = groupService;
	}

	@GetMapping
	public String findAll(Model model) {
		model.addAttribute("groups", groupService.findAll());
		return "group/findAll";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") int id, Model model) {
		model.addAttribute("group", groupService.findById(id));
		return "group/findById";
	}

	@GetMapping("/new")
	public String create(@ModelAttribute("group") GroupDto groupDto) {
		return "group/new";
	}

	@PostMapping
	public String save(@ModelAttribute("group") @Valid GroupDto groupDto, 
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "group/new";
		}

		if (groupService.checkIfGroupExists(groupDto.getName())) {
			model.addAttribute("errorMessage", "Sorry! Group with this name already exists.");
			return "group/new";
		}
		groupService.save(groupDto);
		return "redirect:/groups";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("group", groupService.findById(id));
		return "group/edit";
	}

	@PostMapping("/{id}")
	public String update(@ModelAttribute("group") GroupDto groupDto, @PathVariable("id") int id, Model model) {
		if (groupService.checkIfGroupExists(groupDto.getName())) {
			model.addAttribute("errorMessage", "Sorry! Group with this name already exists.");
			return "group/edit";
		}
		groupService.update(groupDto, id);
		return "redirect:/groups";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") int id, Model model) {
		model.addAttribute("group", groupService.findById(id));
		if (groupService.checkIfGroupIsNotEmpty(id)) {
			model.addAttribute("errorMessage", "Sorry! You can't delete group that is not empty.");
			return "group/findById";
		}
		groupService.delete(id);
		return "redirect:/groups";
	}

}
