package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.model.Group;
import spring.model.Student;
import spring.service.GroupService;
import spring.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "student/findAll";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("group", groupService.findGroupByStudentId(id));
        return "student/findById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "student/new";
    }

    @PostMapping("/new")
    public String save(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.findAll());
        if (!studentService.checkIfClassIdExists(student.getClassId())) {
            model.addAttribute("errorMessage", "Sorry! Group with this Id doesn't exist.");
            return "student/new";
        }
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/changeGroup")
    public String changeGroup(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("groups", groupService.findAll());
        return "student/changeGroup";
    }

    @PostMapping("/{id}/changeGroup")
    public String updateGroup(@ModelAttribute("student") Student student, @PathVariable("id") int id, Model model) {
        model.addAttribute("groups", groupService.findAll());
        if (!studentService.checkIfClassIdExists(student.getClassId())) {
            model.addAttribute("errorMessage", "Sorry! Group with this Id doesn't exist.");
            return "student/changeGroup";
        }
        studentService.setGroup(student, id);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("student", studentService.findById(id));
        return "student/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("student") Student student, @PathVariable("id") int id) {
        studentService.update(student, id);
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        studentService.delete(id);
        return "redirect:/students";
    }

}
