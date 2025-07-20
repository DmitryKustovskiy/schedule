package spring.exception;

import org.hibernate.StaleObjectStateException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/error";
	}
	
	@ExceptionHandler({OptimisticLockException.class, StaleObjectStateException.class})
	public String handleOptimisticLockException(Model model) {
		model.addAttribute("errorMessage", 
				"The data was modified by another administrator. Come back later!");
		return "error/optimisticError";
	}

}
