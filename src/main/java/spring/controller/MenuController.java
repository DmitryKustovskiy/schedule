package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.service.GroupService;
import spring.service.SubjectService;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@GetMapping
	public String menu(Model model) {
		return "mainMenu";
	}
}
