package spring.listener;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationEvents {
	
	@EventListener
	public void handleLoginSuccess(AuthenticationSuccessEvent event) {
		
		String username = event.getAuthentication().getName();
		log.info("{} logged in at {}", username, LocalDateTime.now());	
	}
	
	@EventListener
	public void handleLogoutSuccess(LogoutSuccessEvent event) {
		
		String username = event.getAuthentication().getName();
		log.info("{} logged out at {}", username, LocalDateTime.now());
	}

}
