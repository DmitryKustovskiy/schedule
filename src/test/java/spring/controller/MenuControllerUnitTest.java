package spring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MenuControllerUnitTest {
	
	@Test
	void shouldReturnMainMenuPage() {
		var menuController = new MenuController();
		String actualResult = menuController.menu();
		
		assertThat(actualResult).isEqualTo("mainMenu");
		
	}

}
