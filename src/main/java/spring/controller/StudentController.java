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
import spring.dto.StudentDto;
import spring.dto.StudentUpdateDto;
import spring.service.GroupService;
import spring.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	private final GroupService groupService;

	@GetMapping
	public String findAll(Model model) {
		model.addAttribute("students", studentService.findAll());
		return "student/findAll";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") int id, Model model) {
		model.addAttribute("student", studentService.findById(id));
		model.addAttribute("group", groupService.findGroupByStudentGroupId(id));
		return "student/findById";
	}

	@GetMapping("/new")
	public String create(@ModelAttribute("student") StudentDto studentDto, Model model) {
		model.addAttribute("groups", groupService.findAll());
		return "student/new";
	}

	@PostMapping("/new")
	public String save(@ModelAttribute("student") @Valid StudentDto studentDto, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("groups", groupService.findAll());
			return "student/new";
		}
		studentService.save(studentDto);
		return "redirect:/students";
	}

	@GetMapping("/{id}/changeGroup")
	public String changeGroup(@PathVariable("id") int id, Model model) {
		StudentDto studentDto = studentService.findById(id);
		model.addAttribute("studentDto", studentDto);
		model.addAttribute("groups", groupService.findAll());
		model.addAttribute("groupDto", groupService.findGroupByStudentGroupId(id));
		return "student/changeGroup";
	}

	@PostMapping("/{id}/changeGroup")
	public String updateGroup(@ModelAttribute("student") StudentDto studentDto, @PathVariable("id") int id) {
		studentService.setGroup(studentDto, id);
		return "redirect:/students";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("student", studentService.findById(id));
		return "student/edit";
	}

	@PostMapping("/{id}")
	public String update(@ModelAttribute("student") @Valid StudentUpdateDto studentUpdateDto, BindingResult bindingResult,
			@PathVariable("id") int id) {
		if (bindingResult.hasErrors()) {
			return "student/edit";
		}
		
		studentService.update(studentUpdateDto, id);
		return "redirect:/students";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") int id) {
		studentService.delete(id);
		return "redirect:/students";
	}

}
