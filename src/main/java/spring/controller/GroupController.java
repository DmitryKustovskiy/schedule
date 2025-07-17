package spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.dto.GroupDto;
import spring.exception.EntityAlreadyExistsException;
import spring.exception.GroupNotEmptyException;
import spring.service.GroupService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

	private final GroupService groupService;

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
	public String save(@ModelAttribute("group") @Valid GroupDto groupDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "group/new";
		}

		try {
			groupService.save(groupDto);
			return "redirect:/groups";
		} catch (EntityAlreadyExistsException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			return "group/new";
		}

	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("group", groupService.findById(id));
		return "group/edit";
	}

	@PostMapping("/{id}")
	public String update(@ModelAttribute("group") @Valid GroupDto groupDto, BindingResult bindingResult,
			@PathVariable("id") int id, Model model) {
		if (bindingResult.hasErrors()) {
			return "group/edit";
		}
		
		try {
			groupService.update(groupDto, id);
			return "redirect:/groups";
		} catch (EntityAlreadyExistsException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			return "group/edit";
		}

	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") int id, Model model) {
		
		try {
			groupService.delete(id);
			return "redirect:/groups";
		} catch (GroupNotEmptyException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			model.addAttribute("group", groupService.findById(id));
			return "group/findById";
		}

	}

}
