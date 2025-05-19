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
import spring.dto.SubjectDto;
import spring.model.Subject;
import spring.service.ScheduleItemService;
import spring.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
	private final SubjectService subjectService;

	@Autowired
	public SubjectController(SubjectService subjectService, ScheduleItemService scheduleItemService) {
		this.subjectService = subjectService;
	}

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

		if (subjectService.checkIfSubjectExists(subjectDto.getName())) {
			model.addAttribute("errorMessage", "Sorry! Subject with this name already exists.");
			return "subject/new";
		}
		subjectService.save(subjectDto);
		return "redirect:/subjects";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("subject", subjectService.findById(id));
		return "subject/edit";
	}

	@PostMapping("/{id}")
	public String update(@ModelAttribute("subject") SubjectDto subjectDto, @PathVariable("id") int id, Model model) {
		if (subjectService.checkIfSubjectExists(subjectDto.getName())) {
			model.addAttribute("errorMessage", "Sorry! Subject with this name already exists!");
			return "subject/edit";
		}
		subjectService.update(subjectDto, id);
		return "redirect:/subjects";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") int id) {
		subjectService.delete(id);
		return "redirect:/subjects";
	}

}
