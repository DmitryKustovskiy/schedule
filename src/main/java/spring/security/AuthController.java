package spring.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final RegistrationService registrationService;

	@GetMapping("/login")
	public String loginPage() {
		return "login";
		
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registrationDto", new RegistrationDto());
		return "register";
		
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registrationDto") @Valid RegistrationDto dto,
			BindingResult bindingResult) {
		if(registrationService.userExists(dto.getUsername())) {
			bindingResult.rejectValue
			("username", "error.username", "User with this login already exists");
		}
		
		if(bindingResult.hasErrors()) {
			return "/register";
		}

		registrationService.register(dto.getUsername(), dto.getPassword(), dto.getRole());
		return "redirect:/login";
		
	}

}
