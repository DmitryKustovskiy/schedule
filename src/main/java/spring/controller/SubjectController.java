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
import lombok.RequiredArgsConstructor;
import spring.dto.SubjectDto;
import spring.exception.EntityAlreadyExistsException;
import spring.model.Subject;
import spring.service.GroupService;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

	private final SubjectService subjectService;

	@GetMapping
	public String findAll(Model model) {
		model.addAttribute("subjects", subjectService.findAll());
		return "subject/findAll";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") int id, Model model) {
		model.addAttribute("subject", subjectService.findById(id));
		return "subject/findById";
	}

	@GetMapping("/new")
	public String create(@ModelAttribute("subject") SubjectDto subjectDto) {
		return "subject/new";
	}

	@PostMapping
	public String save(@ModelAttribute("subject") @Valid SubjectDto subjectDto, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "subject/new";
		}
		
		try {
			subjectService.save(subjectDto);
			return "redirect:/subjects";
		} catch (EntityAlreadyExistsException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			return "subject/new";
		}

	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("subject", subjectService.findById(id));
		return "subject/edit";
	}

	@PostMapping("/{id}")
	public String update(@ModelAttribute("subject") @Valid SubjectDto subjectDto, BindingResult bindingResult,
			@PathVariable("id") int id, Model model) {
		if (bindingResult.hasErrors()) {
			return "subject/edit";
		}

		try {
			subjectService.update(subjectDto, id);
			return "redirect:/subjects";
		} catch (EntityAlreadyExistsException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			return "subject/new";
		}

	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") int id) {
		subjectService.delete(id);
		return "redirect:/subjects";
	}

}
