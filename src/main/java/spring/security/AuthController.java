package spring.security;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String registerPage() {
	    return "register";
	}

	@PostMapping("/register")
	public String register(@RequestParam ("username") String username, @RequestParam ("password") String password) {
        registrationService.register(username, password);
        return "redirect:/login";
    }

}
