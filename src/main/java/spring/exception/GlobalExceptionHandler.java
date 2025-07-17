package spring.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/error";
	}

}
